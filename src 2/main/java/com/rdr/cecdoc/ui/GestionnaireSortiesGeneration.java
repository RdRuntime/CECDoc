package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.model.*;
import com.rdr.cecdoc.service.export.ServiceGenerationLettreGreffiere;
import com.rdr.cecdoc.service.export.ServicePdfDossierComplet;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

final class GestionnaireSortiesGeneration {
    private final JFrame proprietaire;
    private final Supplier<Path> dossierSortieParDefaut;
    private final Function<String, File> fichierParDefautDansDossierSortie;

    GestionnaireSortiesGeneration(JFrame proprietaire, Supplier<Path> dossierSortieParDefaut, Function<String, File> fichierParDefautDansDossierSortie) {
        this.proprietaire = Objects.requireNonNull(proprietaire, "proprietaire");
        this.dossierSortieParDefaut = Objects.requireNonNull(dossierSortieParDefaut, "dossierSortieParDefaut");
        this.fichierParDefautDansDossierSortie = Objects.requireNonNull(fichierParDefautDansDossierSortie, "fichierParDefautDansDossierSortie");
    }

    File choisirDestinationRequete() {
        JFileChooser selecteurFichier = creerSelecteurFichier("Enregistrer la requête", false);
        TypeDocumentGenere typeParDefaut = TypeDocumentGenere.DOCX;
        selecteurFichier.setSelectedFile(fichierParDefautDansDossierSortie.apply(SelectionFichierDocument.adapterNomFichier("requete_changement_sexe.docx", typeParDefaut)));
        SelectionFichierDocument.appliquerFiltresDocuments(selecteurFichier, "Requête", typeParDefaut);
        int choix = selecteurFichier.showSaveDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        TypeDocumentGenere typeSelectionne = SelectionFichierDocument.typeDepuisSelection(selecteurFichier);
        return SelectionFichierDocument.garantirExtension(selecteurFichier.getSelectedFile(), typeSelectionne);
    }

    File choisirDestinationDossierPdf(Path cheminRequete) {
        JFileChooser selecteurFichier = creerSelecteurFichier("Enregistrer le dossier PDF", false);
        selecteurFichier.setSelectedFile(ServicePdfDossierComplet.cheminPdfDossier(cheminRequete).toFile());
        selecteurFichier.setFileFilter(new FileNameExtensionFilter("Document PDF (*.pdf)", "pdf"));
        int choix = selecteurFichier.showSaveDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        return SelectionFichierDocument.garantirExtension(selecteurFichier.getSelectedFile(), "pdf");
    }

    File choisirDestinationLettreGreffiere(Path cheminRequete) {
        TypeDocumentGenere typeParDefaut = TypeDocumentGenere.depuisChemin(cheminRequete);
        JFileChooser selecteurFichier = creerSelecteurFichier("Enregistrer la lettre greffier·e", false);
        selecteurFichier.setSelectedFile(ServiceGenerationLettreGreffiere.cheminParDefaut(cheminRequete, typeParDefaut).toFile());
        SelectionFichierDocument.appliquerFiltresDocuments(selecteurFichier, "Lettre greffier·e", typeParDefaut);
        int choix = selecteurFichier.showSaveDialog(proprietaire);
        if (choix != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        MemoireRepertoireExplorateur.memoriserDepuisSelection(selecteurFichier);
        TypeDocumentGenere typeSelectionne = SelectionFichierDocument.typeDepuisSelection(selecteurFichier);
        return SelectionFichierDocument.garantirExtension(selecteurFichier.getSelectedFile(), typeSelectionne);
    }

    boolean confirmerEcrasementSorties(File fichierSortieRequete, File fichierSortiePdf, File fichierSortieLettreGreffiere, DemandeConfirmation demandeConfirmation) {
        List<String> sortiesExistantes = new ArrayList<>(3);
        if (fichierSortieRequete != null && fichierSortieRequete.exists()) {
            sortiesExistantes.add("Requête");
        }
        if (fichierSortiePdf != null && fichierSortiePdf.exists()) {
            sortiesExistantes.add("PDF");
        }
        if (fichierSortieLettreGreffiere != null && fichierSortieLettreGreffiere.exists()) {
            sortiesExistantes.add("Lettre greffier·e");
        }

        if (sortiesExistantes.isEmpty()) {
            return true;
        }

        String message;
        if (sortiesExistantes.size() == 1) {
            message = "Le fichier " + sortiesExistantes.get(0) + " existe déjà. Voulez-vous le remplacer ?";
        } else {
            message = "Des fichiers de sortie existent déjà (" + String.join(", ", sortiesExistantes) + "). Voulez-vous les remplacer ?";
        }

        int choix = demandeConfirmation.demander(message, "Confirmer l'écrasement", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        return choix == JOptionPane.YES_OPTION;
    }

    boolean aDesPiecesJointesExportables(DonneesDossier donneesDossier) {
        if (donneesDossier == null || donneesDossier.piecesJustificativesDetaillees().isEmpty()) {
            return false;
        }

        for (PieceJustificative piece : donneesDossier.piecesJustificativesDetaillees()) {
            if (piece == null || piece.piecesJointes().isEmpty()) {
                continue;
            }
            for (PieceJointe pieceJointe : piece.piecesJointes()) {
                if (pieceJointe == null) {
                    continue;
                }
                TypePieceJointe type = pieceJointe.type();
                if (type == null || type == TypePieceJointe.INCONNU) {
                    type = TypePieceJointe.depuisNomFichier(pieceJointe.nomVisible());
                }
                if (type.estPrisEnCharge()) {
                    return true;
                }
            }
        }

        return false;
    }

    private JFileChooser creerSelecteurFichier(String titre, boolean multiselection) {
        JFileChooser selecteur = new JFileChooser();
        MemoireRepertoireExplorateur.appliquerAuSelecteur(selecteur, dossierSortieParDefaut.get());
        selecteur.setDialogTitle(titre);
        selecteur.setMultiSelectionEnabled(multiselection);
        selecteur.setAcceptAllFileFilterUsed(false);
        return selecteur;
    }

    @FunctionalInterface
    interface DemandeConfirmation {
        int demander(String message, String titre, int optionType, int messageType);
    }
}

package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;
import com.rdr.cecdoc.util.ParseursDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public final class DonneesLettreMiseAJourActesLies {
    private final String prenom;
    private final String nom;
    private final String adressePostale;
    private final String telephonePortable;
    private final String courriel;
    private final String typeDestinataire;
    private final String villeAutoriteDestinataire;
    private final String adresseDestinataire;
    private final String villeRedaction;
    private final String dateRedaction;
    private final String genreAccords;
    private final String dateNaissance;
    private final String lieuNaissance;
    private final String autoriteDecision;
    private final String dateDecision;
    private final String dateDecisionDefinitive;
    private final boolean changementPrenoms;
    private final boolean changementSexe;
    private final boolean acteNaissanceRequerant;
    private final String communeNaissanceRequerant;
    private final String anneeNaissanceRequerant;
    private final boolean concernePartenaire;
    private final String lienPartenaire;
    private final String genrePartenaire;
    private final boolean acteMariage;
    private final String communeMariage;
    private final String dateMariage;
    private final boolean acteNaissancePartenaire;
    private final String prenomPartenaire;
    private final String nomPartenaire;
    private final String communeNaissancePartenaire;
    private final String anneeNaissancePartenaire;
    private final boolean mentionPacs;
    private final String autoritePacs;
    private final String optionLivret;
    private final List<DonneesEnfantActe> enfants;

    public DonneesLettreMiseAJourActesLies(String prenom, String nom, String adressePostale, String telephonePortable, String courriel, String villeAutoriteDestinataire, String adresseDestinataire, String villeRedaction, String dateRedaction, String genreAccords, String dateNaissance, String lieuNaissance, String autoriteDecision, String dateDecision, String dateDecisionDefinitive, boolean changementPrenoms, boolean changementSexe, boolean acteNaissanceRequerant, String communeNaissanceRequerant, String anneeNaissanceRequerant, boolean concernePartenaire, String lienPartenaire, String genrePartenaire, boolean acteMariage, String communeMariage, String dateMariage, boolean acteNaissancePartenaire, String prenomPartenaire, String nomPartenaire, String communeNaissancePartenaire, String anneeNaissancePartenaire, boolean mentionPacs, String autoritePacs, String optionLivret, List<DonneesEnfantActe> enfants) {
        this.prenom = NormalisationTexte.normaliserNomPropre(prenom);
        this.nom = NormalisationTexte.normaliserNomPropre(nom);
        this.adressePostale = NormalisationTexte.normaliserTexte(adressePostale);
        this.telephonePortable = NormalisationTexte.normaliserTexte(telephonePortable);
        this.courriel = NormalisationTexte.normaliserTexte(courriel).toLowerCase(Locale.ROOT);
        this.typeDestinataire = normaliserTypeDestinataire();
        this.villeAutoriteDestinataire = NormalisationTexte.normaliserNomPropre(villeAutoriteDestinataire);
        this.adresseDestinataire = NormalisationTexte.normaliserTexte(adresseDestinataire);
        this.villeRedaction = NormalisationTexte.normaliserNomPropre(villeRedaction);
        this.dateRedaction = NormalisationTexte.normaliserTexte(dateRedaction);
        this.genreAccords = normaliserGenre(genreAccords);
        this.dateNaissance = NormalisationTexte.normaliserTexte(dateNaissance);
        this.lieuNaissance = NormalisationTexte.normaliserNomPropre(lieuNaissance);
        this.autoriteDecision = normaliserAutoriteDecision(autoriteDecision);
        this.dateDecision = NormalisationTexte.normaliserTexte(dateDecision);
        this.dateDecisionDefinitive = NormalisationTexte.normaliserTexte(dateDecisionDefinitive);
        this.changementPrenoms = changementPrenoms;
        this.changementSexe = changementSexe && !"mairie".equals(this.autoriteDecision);
        this.acteNaissanceRequerant = acteNaissanceRequerant;
        this.communeNaissanceRequerant = NormalisationTexte.normaliserNomPropre(communeNaissanceRequerant);
        this.anneeNaissanceRequerant = NormalisationTexte.normaliserTexte(anneeNaissanceRequerant);
        this.concernePartenaire = concernePartenaire;
        this.lienPartenaire = normaliserLienPartenaire(lienPartenaire);
        this.genrePartenaire = normaliserGenre(genrePartenaire);
        this.acteMariage = acteMariage;
        this.communeMariage = NormalisationTexte.normaliserNomPropre(communeMariage);
        this.dateMariage = NormalisationTexte.normaliserTexte(dateMariage);
        this.acteNaissancePartenaire = acteNaissancePartenaire;
        this.prenomPartenaire = NormalisationTexte.normaliserNomPropre(prenomPartenaire);
        this.nomPartenaire = NormalisationTexte.normaliserNomPropre(nomPartenaire);
        this.communeNaissancePartenaire = NormalisationTexte.normaliserNomPropre(communeNaissancePartenaire);
        this.anneeNaissancePartenaire = NormalisationTexte.normaliserTexte(anneeNaissancePartenaire);
        this.mentionPacs = mentionPacs;
        this.autoritePacs = NormalisationTexte.normaliserTexte(autoritePacs);
        this.optionLivret = normaliserOptionLivret(optionLivret);
        this.enfants = enfants == null ? List.of() : enfants.stream().filter(Objects::nonNull).toList();
    }

    public String identiteEntete() {
        if (prenom.isBlank()) {
            return nomMajuscules();
        }
        if (nom.isBlank()) {
            return prenom;
        }
        return prenom + " " + nomMajuscules();
    }

    public String nomMajuscules() {
        return nom.toUpperCase(Locale.ROOT);
    }

    public String ligneContact() {
        if (telephonePortable.isBlank() && courriel.isBlank()) {
            return "";
        }
        if (telephonePortable.isBlank()) {
            return courriel;
        }
        if (courriel.isBlank()) {
            return telephonePortable;
        }
        return telephonePortable + " – " + courriel;
    }

    public String adressePostale() {
        return adressePostale;
    }

    public boolean destinataireProcureur() {
        return "procureur".equals(typeDestinataire);
    }

    public String ligneDestinataireTitre() {
        if (destinataireProcureur()) {
            return "À l’attention de Madame/Monsieur le/la Procureur·e de la République";
        }
        return "À l’attention de Madame/Monsieur l’Officier·e de l’état civil";
    }

    public String ligneDestinataireSousTitre() {
        if (destinataireProcureur()) {
            return "près le Tribunal judiciaire de " + villeAutoriteDestinataire;
        }
        return "Mairie de " + villeAutoriteDestinataire;
    }

    public String adresseDestinataire() {
        return adresseDestinataire;
    }

    public String villeRedaction() {
        return villeRedaction;
    }

    public String dateRedaction() {
        return dateRedaction;
    }

    public String dateNaissance() {
        return dateNaissance;
    }

    public String lieuNaissance() {
        return lieuNaissance;
    }

    public String adjectifSousSigne() {
        return switch (genreAccords) {
            case "féminin" -> "soussignée";
            case "non-binaire" -> "soussigné·e";
            default -> "soussigné";
        };
    }

    public String adjectifNe() {
        return switch (genreAccords) {
            case "féminin" -> "née";
            case "non-binaire" -> "né·e";
            default -> "né";
        };
    }

    public String formuleDecisionRenduePar() {
        if ("mairie".equals(autoriteDecision)) {
            return "l’Officier·e d’état civil";
        }
        return "le Tribunal judiciaire de " + villeAutoriteDestinataire;
    }

    public String formuleSuiteActeDecisionDefinitif() {
        if ("mairie".equals(autoriteDecision)) {
            return "d’une décision définitive";
        }
        return "d’un jugement définitif";
    }

    public String formuleSuiteActeDecisionRenduPar() {
        if ("mairie".equals(autoriteDecision)) {
            return "de la décision rendue par " + formuleDecisionRenduePar();
        }
        return "du jugement rendu par " + formuleDecisionRenduePar();
    }

    public String formuleActeDecisionPrecite() {
        if ("mairie".equals(autoriteDecision)) {
            return "La décision précitée";
        }
        return "Le jugement précité";
    }

    public String formuleActeDecisionPieceJointe() {
        if ("mairie".equals(autoriteDecision)) {
            return "de la décision";
        }
        return "du jugement";
    }

    public String formuleActeDecisionDevenueDefinitive() {
        if ("mairie".equals(autoriteDecision)) {
            return "la décision devenue définitive";
        }
        return "le jugement devenu définitif";
    }

    public String formuleActeDecisionDevenuDefinitif() {
        if ("mairie".equals(autoriteDecision)) {
            return "devenue définitive";
        }
        return "devenu définitif";
    }

    public String dateDecision() {
        return dateDecision;
    }

    public String dateDecisionDefinitive() {
        return dateDecisionDefinitive;
    }

    public boolean changementPrenoms() {
        return changementPrenoms;
    }

    public boolean changementSexe() {
        return changementSexe;
    }

    public String objetTypeChangements() {
        if (changementPrenoms && changementSexe) {
            return "changement de prénom et de mention du sexe";
        }
        if (changementPrenoms) {
            return "changement de prénom";
        }
        if (changementSexe) {
            return "modification de la mention du sexe";
        }
        return "mise à jour corrélative";
    }

    public boolean acteNaissanceRequerant() {
        return acteNaissanceRequerant;
    }

    public String ligneActeNaissanceRequerant() {
        return "acte de naissance de " + identiteEntete() + " (commune de naissance : " + communeNaissanceRequerant + ", année : " + anneeNaissanceRequerantEffective() + ")";
    }

    public boolean concernePartenaire() {
        return concernePartenaire;
    }

    public String designationPartenaire() {
        if ("partenaire_pacs".equals(lienPartenaire)) {
            return switch (genrePartenaire) {
                case "féminin" -> "partenaire de PACS";
                case "non-binaire" -> "partenaire de PACS";
                default -> "partenaire de PACS";
            };
        }
        return switch (genrePartenaire) {
            case "féminin" -> "épouse";
            case "non-binaire" -> "époux·se";
            default -> "époux";
        };
    }

    public String determinantPartenaire() {
        return "mon";
    }

    public boolean acteMariage() {
        return acteMariage;
    }

    public String ligneActeMariage() {
        return "acte de mariage (commune : " + communeMariage + ", date : " + dateMariage + ")";
    }

    public boolean acteNaissancePartenaire() {
        return acteNaissancePartenaire;
    }

    public String ligneActeNaissancePartenaire() {
        return "acte de naissance de " + identitePartenaire() + " (commune : " + communeNaissancePartenaire + ", année : " + anneeNaissancePartenaire + ")";
    }

    public boolean mentionPacs() {
        return mentionPacs;
    }

    public String ligneMentionPacs() {
        return "mention de PACS apposée en marge (commune/autorité dépositaire : " + autoritePacs + ")";
    }

    public List<DonneesEnfantActe> enfants() {
        return enfants;
    }

    public boolean aDesEnfants() {
        return !enfants.isEmpty();
    }

    public boolean optionLivretRenseignee() {
        return !"aucune".equals(optionLivret);
    }

    public String ligneLivret() {
        return switch (optionLivret) {
            case "mise_a_jour" -> "livret de famille : demande de mise à jour";
            case "nouveau" -> "livret de famille : demande d’établissement d’un nouveau livret";
            default -> "livret de famille : aucune demande";
        };
    }

    public String formuleDemandeLivret() {
        return switch (optionLivret) {
            case "mise_a_jour" -> "la mise à jour du livret de famille";
            case "nouveau" -> "la délivrance d’un nouveau livret de famille";
            default -> "la mise à jour corrélative des actes";
        };
    }

    public String objetMiseAJourActes() {
        List<String> actes = actesDemandesSynthese();
        if (actes.isEmpty()) {
            return "Demande de mise à jour d’actes d’état civil";
        }
        if (actes.size() == 1) {
            return "Demande de mise à jour de " + actes.get(0);
        }
        return "Demande de mise à jour des actes d’état civil";
    }

    public String formuleDemandeInitiale() {
        List<String> actes = actesDemandesSynthese();
        if (actes.isEmpty()) {
            return "la mise à jour des actes d’état civil me concernant";
        }
        if (actes.size() == 1) {
            return "la mise à jour de " + actes.get(0);
        }
        return "la mise à jour des actes d’état civil listés ci-dessous";
    }

    public String formuleRappelDemande() {
        List<String> actes = actesDemandesSynthese();
        if (actes.isEmpty()) {
            return "la mise à jour corrélative des actes d’état civil suivants";
        }
        if (actes.size() == 1) {
            return "la mise à jour corrélative de cet acte";
        }
        return "la mise à jour corrélative des actes d’état civil suivants";
    }

    public String signature() {
        return identiteEntete();
    }

    private String identitePartenaire() {
        if (prenomPartenaire.isBlank()) {
            return nomPartenaire.toUpperCase(Locale.ROOT);
        }
        if (nomPartenaire.isBlank()) {
            return prenomPartenaire;
        }
        return prenomPartenaire + " " + nomPartenaire.toUpperCase(Locale.ROOT);
    }

    private String anneeNaissanceRequerantEffective() {
        if (!anneeNaissanceRequerant.isBlank()) {
            return anneeNaissanceRequerant;
        }
        if (ParseursDate.dateSaisieValide(dateNaissance)) {
            return Integer.toString(ParseursDate.parserDateSaisie(dateNaissance).getYear());
        }
        return "";
    }

    private List<String> actesDemandesSynthese() {
        List<String> actes = new ArrayList<>();
        if (acteNaissanceRequerant) {
            actes.add("l’acte de naissance");
        }
        if (concernePartenaire && acteMariage) {
            actes.add("l’acte de mariage");
        }
        if (concernePartenaire && acteNaissancePartenaire) {
            actes.add("l’acte de naissance de " + determinantPartenaire() + " " + designationPartenaire());
        }
        if (concernePartenaire && mentionPacs) {
            actes.add("la mention de PACS");
        }
        if (aDesEnfants()) {
            actes.add("les actes de naissance des enfants");
        }
        if (optionLivretRenseignee()) {
            actes.add("le livret de famille");
        }
        return actes;
    }

    private String normaliserTypeDestinataire() {
        return "mairie";
    }

    private String normaliserAutoriteDecision(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.contains("officier")) {
            return "mairie";
        }
        return "tribunal";
    }

    private String normaliserGenre(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.startsWith("f")) {
            return "féminin";
        }
        if (texte.startsWith("n")) {
            return "non-binaire";
        }
        return "masculin";
    }

    private String normaliserLienPartenaire(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.contains("pacs")) {
            return "partenaire_pacs";
        }
        return "epoux";
    }

    private String normaliserOptionLivret(String valeur) {
        String texte = NormalisationTexte.normaliserTexte(valeur).toLowerCase(Locale.ROOT);
        if (texte.contains("établissement") || texte.contains("nouveau")) {
            return "nouveau";
        }
        if (texte.contains("mise")) {
            return "mise_a_jour";
        }
        return "aucune";
    }
}

package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;
import org.checkerframework.checker.nullness.qual.NonNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.Objects;

public final class DialogueAide extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;
    private static final String TEXTE_AIDE = """
            CECDoc permet de préparer un dossier de changement à l’état civil et plusieurs courriers associés, avec synchronisation des données entre formulaires.
            
            Barre de menus :
            • Fichiers : paramètres, import et export de configuration ;
            • Thème : sélection du thème visuel ;
            • Aide : guide, recherche de mise à jour, don et fenêtre "À propos".
            
            Formulaire principal :
            • les champs obligatoires sont validés avant génération ;
            • les dates sont saisies via des sélecteurs jour/mois/année validés ;
            • le format de sortie principal (.docx ou .odt) se choisit dans le sélecteur d’enregistrement ;
            • le bouton "Pièces justificatives" ouvre la gestion des pièces ;
            • le bouton "Autres documents" ouvre les formulaires complémentaires ;
            • le bouton "Effacer les données" réinitialise la saisie.
            
            Génération :
            • la requête principale est toujours générée ;
            • le PDF du dossier complet n’est proposé que si au moins une pièce justificative est attachée ;
            • l’ordre du PDF suit l’ordre des intitulés puis l’ordre des fichiers ;
            • la lettre greffier·e est intégrée en tête du PDF quand le PDF est généré.
            
            Fenêtre "Pièces justificatives" :
            • ajout, remplacement, suppression et réordonnancement des fichiers ;
            • formats acceptés : DOC, DOCX, ODT, PDF, JPG, JPEG, PNG ;
            • signalement des fichiers introuvables avec possibilité de corriger ;
            • mémorisation du dernier dossier parcouru dans les sélecteurs.
            
            Fenêtre "Autres documents" :
            • Lettre université ;
            • Lettre administration ;
            • Demande de changement de prénoms (ouverture Service-Public) ;
            • Recours refus de changement de prénom ;
            • Recours refus de changement de sexe ;
            • Relance tribunal judiciaire ;
            • Relance mairie pour changement de prénoms ;
            • Mise à jour des actes liés ;
            • Lettre RGPD / minimisation.
            
            Fenêtre "Configuration" :
            • confirmation avant fermeture ;
            • mémorisation des saisies ;
            • dossier de sortie par défaut (sélecteur de dossier).
            
            Configuration fichier :
            • stockage local dans .cecdoc.conf ;
            • import accepté : .xml ;
            • export : .xml.
            
            Raccourcis clavier principaux :
            • Ctrl/Cmd + Entrée : générer ;
            • Ctrl/Cmd + J : ouvrir "Pièces justificatives" ;
            • Ctrl/Cmd + O : ouvrir "Autres documents" ;
            • Ctrl/Cmd + I ou F1 : ouvrir l’aide ;
            • Ctrl/Cmd + , : ouvrir les paramètres ;
            • Ctrl/Cmd + Q : quitter l’application ;
            • Ctrl/Cmd + Maj + L : activer/désactiver le changement de prénoms ;
            • Ctrl/Cmd + Z : annuler la saisie dans un champ texte ;
            • Échap : effacer les messages de validation en ligne.
            """;
    private final transient TokensTheme theme;

    public DialogueAide(JFrame owner, TokensTheme theme) {
        super(owner, "Comment fonctionne l'application", true);
        this.theme = Objects.requireNonNull(theme, "theme");
        if (owner != null && !owner.getIconImages().isEmpty()) {
            setIconImages(owner.getIconImages());
        }
        construireInterface();
        setSize(760, 520);
        setMinimumSize(new Dimension(640, 410));
        setLocationRelativeTo(owner);
    }

    private void construireInterface() {
        JPanel racine = new JPanel(new BorderLayout(0, theme.spacing().blockGap()));
        racine.setBorder(new EmptyBorder(theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset(), theme.spacing().cardInset()));
        racine.setBackground(theme.palette().surfaceBackground());

        JLabel titre = new JLabel("Guide rapide d'utilisation");
        titre.setFont(theme.typography().section());
        titre.setForeground(theme.palette().titleText());
        racine.add(titre, BorderLayout.NORTH);

        JTextArea contenu = getJTextArea();

        JScrollPane ascenseur = new JScrollPane(contenu);
        int marge = Math.max(1, theme.spacing().inlineGap() / 4);
        ascenseur.setBorder(BorderFactory.createCompoundBorder(new LineBorder(theme.palette().border(), 1, true), new EmptyBorder(marge, marge, marge, marge)));
        racine.add(ascenseur, BorderLayout.CENTER);

        JButton boutonFermer = new JButton("Fermer");
        boutonFermer.setMnemonic(KeyEvent.VK_F);
        StyliseurBoutonTheme.appliquer(boutonFermer, theme.palette().primaryButton(), theme, theme.typography().buttonPrimary());
        boutonFermer.addActionListener(e -> dispose());

        JPanel actions = new JPanel(new BorderLayout());
        actions.setOpaque(false);
        actions.add(boutonFermer, BorderLayout.EAST);
        racine.add(actions, BorderLayout.SOUTH);

        setContentPane(racine);

        getRootPane().setDefaultButton(boutonFermer);
        getRootPane().registerKeyboardAction(e -> dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        getAccessibleContext().setAccessibleName("Aide de l'application");
        getAccessibleContext().setAccessibleDescription("Fenêtre d'aide décrivant la saisie, la génération et les options de CECDoc");
        contenu.getAccessibleContext().setAccessibleName("Contenu de l'aide");
        contenu.getAccessibleContext().setAccessibleDescription("Texte explicatif sur les formulaires, la génération des documents et la configuration");
        ascenseur.getAccessibleContext().setAccessibleName("Zone de lecture de l'aide");
        boutonFermer.getAccessibleContext().setAccessibleName("Fermer l'aide");

        SwingUtilities.invokeLater(boutonFermer::requestFocusInWindow);
    }

    private @NonNull JTextArea getJTextArea() {
        JTextArea contenu = new JTextArea(TEXTE_AIDE);
        contenu.setEditable(false);
        contenu.setLineWrap(true);
        contenu.setWrapStyleWord(true);
        contenu.setFont(theme.typography().input());
        contenu.setForeground(theme.palette().bodyText());
        contenu.setBackground(theme.palette().fieldBackground());
        contenu.setBorder(new EmptyBorder(theme.spacing().blockGap(), theme.spacing().blockGap(), theme.spacing().blockGap(), theme.spacing().blockGap()));
        contenu.setCaretPosition(0);
        return contenu;
    }
}

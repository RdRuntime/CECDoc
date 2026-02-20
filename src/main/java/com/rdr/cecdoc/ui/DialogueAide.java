package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.StyliseurBoutonTheme;
import com.rdr.cecdoc.ui.theme.TokensTheme;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.io.Serial;
import java.util.Objects;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public final class DialogueAide extends JDialog {
    @Serial
    private static final long serialVersionUID = 1L;
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
        JTextArea contenu = new JTextArea(construireTexteAide());
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

    private String construireTexteAide() {
        return """
                CECDoc permet de préparer et générer un dossier de requête de changement à l'état civil, avec synchronisation des données entre plusieurs formulaires.
                
                Barre de menus :
                • Fichiers : paramètres, import et export de configuration ;
                • Thème : sélection du thème visuel ;
                • Aide : guide, recherche de mise à jour et fenêtre "À propos".
                
                Ce que génère l'application :
                • Requête (.docx ou .odt) : toujours générée ;
                • Dossier PDF complet : proposé uniquement si au moins une pièce justificative est attachée ;
                • Lettre à la·au greffier·e : proposée à la génération de la requête ;
                • Autres documents : lettres "Université" et "Administration" accessibles via le bouton "Autres documents".
                
                Formulaire principal :
                • les champs obligatoires sont validés avant génération ;
                • les dates utilisent des sélecteurs jour/mois/année avec validation ;
                • le bouton "Pièces justificatives" ouvre la gestion détaillée des pièces ;
                • le bouton "Effacer les données" réinitialise la saisie.
                
                Fenêtre "Pièces justificatives" :
                • vous pouvez ajouter, retirer, remplacer et réordonner les fichiers par intitulé ;
                • formats acceptés : DOC, DOCX, ODT, PDF, JPG, JPEG, PNG ;
                • les fichiers introuvables sont signalés et peuvent être corrigés ;
                • l'ordre du PDF suit l'ordre des intitulés puis l'ordre des fichiers affichés.
                
                Fenêtre "Configuration" :
                • confirmation avant fermeture ;
                • mémorisation des saisies dans le fichier .cecdoc.conf ;
                • dossier de sortie par défaut via sélecteur de dossier.

                Choix du format de document :
                • au moment de l'enregistrement, utilisez le type de fichier du sélecteur pour choisir .docx ou .odt.
                
                Raccourcis clavier principaux :
                • Ctrl/Cmd + Entrée : générer ;
                • Ctrl/Cmd + J : ouvrir "Pièces justificatives" ;
                • Ctrl/Cmd + I ou F1 : ouvrir l'aide ;
                • Ctrl/Cmd + , : ouvrir les paramètres ;
                • Ctrl/Cmd + Q : quitter l'application ;
                • Ctrl/Cmd + Maj + L : activer/désactiver le changement de prénoms ;
                • Ctrl/Cmd + Z : annuler la saisie dans un champ texte ;
                • Échap : effacer les messages de validation en ligne.
                """;
    }
}

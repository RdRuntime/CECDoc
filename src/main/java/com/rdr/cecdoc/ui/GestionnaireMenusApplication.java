package com.rdr.cecdoc.ui;

import com.rdr.cecdoc.ui.theme.PreferenceThemeApplication;
import com.rdr.cecdoc.ui.theme.TokensTheme;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.basic.BasicMenuUI;

final class GestionnaireMenusApplication {
    @FunctionalInterface
    interface ActionTheme {
        void appliquer(PreferenceThemeApplication preferenceTheme);
    }

    private static final String CLE_STYLE_SELECTION_MENU = "cecdoc.style.selection.menu";

    private final Supplier<TokensTheme> fournisseurTheme;
    private final Supplier<PreferenceThemeApplication> fournisseurPreferenceTheme;
    private final ActionTheme actionTheme;
    private final Runnable actionParametres;
    private final Runnable actionImporterConfiguration;
    private final Runnable actionExporterConfiguration;
    private final Runnable actionGuide;
    private final Runnable actionRechercheMiseAJour;
    private final Runnable actionAPropos;

    private final Map<PreferenceThemeApplication, JRadioButtonMenuItem> itemsMenuTheme;
    private final List<JMenuItem> itemsMenuActifs;

    private JMenuBar barreMenusPrincipale;

    GestionnaireMenusApplication(
            Supplier<TokensTheme> fournisseurTheme,
            Supplier<PreferenceThemeApplication> fournisseurPreferenceTheme,
            ActionTheme actionTheme,
            Runnable actionParametres,
            Runnable actionImporterConfiguration,
            Runnable actionExporterConfiguration,
            Runnable actionGuide,
            Runnable actionRechercheMiseAJour,
            Runnable actionAPropos
    ) {
        this.fournisseurTheme = Objects.requireNonNull(fournisseurTheme, "fournisseurTheme");
        this.fournisseurPreferenceTheme = Objects.requireNonNull(fournisseurPreferenceTheme, "fournisseurPreferenceTheme");
        this.actionTheme = Objects.requireNonNull(actionTheme, "actionTheme");
        this.actionParametres = Objects.requireNonNull(actionParametres, "actionParametres");
        this.actionImporterConfiguration = Objects.requireNonNull(actionImporterConfiguration, "actionImporterConfiguration");
        this.actionExporterConfiguration = Objects.requireNonNull(actionExporterConfiguration, "actionExporterConfiguration");
        this.actionGuide = Objects.requireNonNull(actionGuide, "actionGuide");
        this.actionRechercheMiseAJour = Objects.requireNonNull(actionRechercheMiseAJour, "actionRechercheMiseAJour");
        this.actionAPropos = Objects.requireNonNull(actionAPropos, "actionAPropos");
        this.itemsMenuTheme = new EnumMap<>(PreferenceThemeApplication.class);
        this.itemsMenuActifs = new ArrayList<>();
    }

    JMenuBar construireBarreMenusPrincipale() {
        if (barreMenusPrincipale != null) {
            return barreMenusPrincipale;
        }

        JMenuBar barre = new JMenuBar();
        barre.getAccessibleContext().setAccessibleName("Barre de menus principale");
        barre.getAccessibleContext().setAccessibleDescription("Donne accès aux fichiers, au thème et à l'aide");

        int masqueMenu = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        JMenu menuFichier = creerMenu("Fichiers", KeyEvent.VK_F, "Menu Fichiers", "Paramètres et import/export de configuration");
        menuFichier.add(creerElementMenu("Paramètres", KeyEvent.VK_P, KeyStroke.getKeyStroke(KeyEvent.VK_COMMA, masqueMenu), actionParametres, "Paramètres", "Ouvre les paramètres de l'application", true));
        menuFichier.add(creerElementMenu("Importer une configuration", KeyEvent.VK_I, null, actionImporterConfiguration, "Importer une configuration", "Importe une configuration CECDoc au format XML ou CONF", true));
        menuFichier.add(creerElementMenu("Exporter une configuration", KeyEvent.VK_E, null, actionExporterConfiguration, "Exporter une configuration", "Exporte la configuration CECDoc au format XML", true));

        JMenu menuTheme = creerMenu("Thème", KeyEvent.VK_T, "Menu Thème", "Choix du thème visuel");
        ButtonGroup groupeThemes = new ButtonGroup();
        for (PreferenceThemeApplication preference : PreferenceThemeApplication.values()) {
            JRadioButtonMenuItem itemTheme = creerRadioTheme(preference);
            groupeThemes.add(itemTheme);
            menuTheme.add(itemTheme);
            itemsMenuTheme.put(preference, itemTheme);
        }

        JMenu menuAide = creerMenu("Aide", KeyEvent.VK_A, "Menu Aide", "Aide et informations sur l'application");
        menuAide.add(creerElementMenu("Guide d'utilisation", KeyEvent.VK_G, KeyStroke.getKeyStroke(KeyEvent.VK_I, masqueMenu), actionGuide, "Guide d'utilisation", "Ouvre la fenêtre d'aide", true));
        menuAide.add(creerElementMenu("Rechercher une mise à jour disponible...", KeyEvent.VK_R, null, actionRechercheMiseAJour, "Rechercher une mise à jour", "Recherche une version plus récente sur le dépôt officiel", true));
        menuAide.add(creerElementMenu("À propos", KeyEvent.VK_P, null, actionAPropos, "À propos", "Affiche les informations générales de l'application", true));

        barre.add(menuFichier);
        barre.add(menuTheme);
        barre.add(menuAide);

        barreMenusPrincipale = barre;
        synchroniserMenusTheme();
        return barre;
    }

    void synchroniserMenusTheme() {
        if (itemsMenuTheme.isEmpty()) {
            return;
        }
        PreferenceThemeApplication preferenceCourante = fournisseurPreferenceTheme.get();
        PreferenceThemeApplication preference = preferenceCourante == null ? PreferenceThemeApplication.DEFAUT : preferenceCourante;
        for (Map.Entry<PreferenceThemeApplication, JRadioButtonMenuItem> entree : itemsMenuTheme.entrySet()) {
            entree.getValue().setSelected(entree.getKey() == preference);
        }
    }

    void appliquerThemeMenus() {
        if (barreMenusPrincipale == null) {
            return;
        }
        TokensTheme theme = fournisseurTheme.get();
        if (theme == null) {
            return;
        }

        appliquerPaletteSelectionMenus(theme);
        barreMenusPrincipale.setFont(theme.typography().label());
        barreMenusPrincipale.setBackground(theme.palette().surfaceBackground());
        barreMenusPrincipale.setForeground(theme.palette().bodyText());
        barreMenusPrincipale.setOpaque(true);
        barreMenusPrincipale.setBorder(
                BorderFactory.createCompoundBorder(
                        new LineBorder(theme.palette().border(), 1, true),
                        new EmptyBorder(3, 6, 3, 6)
                )
        );

        for (int i = 0; i < barreMenusPrincipale.getMenuCount(); i++) {
            JMenu menu = barreMenusPrincipale.getMenu(i);
            if (menu != null) {
                stylerMenuRecursivement(menu, theme);
            }
        }
        barreMenusPrincipale.revalidate();
        barreMenusPrincipale.repaint();
    }

    void mettreAJourDisponibiliteMenus(boolean disponible) {
        for (JMenuItem item : itemsMenuActifs) {
            item.setEnabled(disponible);
        }
    }

    private JMenu creerMenu(String texte, int mnemonic, String nomAccessible, String descriptionAccessible) {
        JMenu menu = new JMenu(texte);
        menu.setMnemonic(mnemonic);
        menu.setContentAreaFilled(false);
        menu.setBorderPainted(false);
        menu.setOpaque(true);
        menu.setUI(new MenuUiSansSurlignage());
        menu.getAccessibleContext().setAccessibleName(nomAccessible);
        menu.getAccessibleContext().setAccessibleDescription(descriptionAccessible);
        return menu;
    }

    private JMenuItem creerElementMenu(
            String texte,
            int mnemonic,
            KeyStroke accelerateur,
            Runnable action,
            String nomAccessible,
            String descriptionAccessible,
            boolean actifPendantTache
    ) {
        JMenuItem item = new JMenuItem(texte);
        item.setMnemonic(mnemonic);
        item.setOpaque(true);
        if (accelerateur != null) {
            item.setAccelerator(accelerateur);
        }
        item.addActionListener(e -> action.run());
        item.getAccessibleContext().setAccessibleName(nomAccessible);
        item.getAccessibleContext().setAccessibleDescription(descriptionAccessible);
        if (actifPendantTache) {
            itemsMenuActifs.add(item);
        }
        return item;
    }

    private JRadioButtonMenuItem creerRadioTheme(PreferenceThemeApplication preferenceTheme) {
        JRadioButtonMenuItem item = new JRadioButtonMenuItem(preferenceTheme.toString());
        item.setOpaque(true);
        item.addActionListener(e -> actionTheme.appliquer(preferenceTheme));
        item.getAccessibleContext().setAccessibleName("Thème " + preferenceTheme);
        item.getAccessibleContext().setAccessibleDescription("Active le thème " + preferenceTheme.toString().toLowerCase(Locale.ROOT));
        itemsMenuActifs.add(item);
        return item;
    }

    private void appliquerPaletteSelectionMenus(TokensTheme theme) {
        ColorUIResource fondSelection = new ColorUIResource(theme.palette().surfaceBackground());
        ColorUIResource texteSelection = new ColorUIResource(theme.palette().bodyText());
        ColorUIResource fondMenu = new ColorUIResource(theme.palette().surfaceBackground());
        ColorUIResource texteMenu = new ColorUIResource(theme.palette().bodyText());
        UIManager.put("MenuBar.background", fondMenu);
        UIManager.put("MenuBar.foreground", texteMenu);
        UIManager.put("MenuBar.selectionBackground", fondMenu);
        UIManager.put("MenuBar.selectionForeground", texteMenu);
        UIManager.put("Menu.selectionBackground", fondSelection);
        UIManager.put("Menu.selectionForeground", texteSelection);
        UIManager.put("MenuItem.selectionBackground", fondSelection);
        UIManager.put("MenuItem.selectionForeground", texteSelection);
        UIManager.put("CheckBoxMenuItem.selectionBackground", fondSelection);
        UIManager.put("CheckBoxMenuItem.selectionForeground", texteSelection);
        UIManager.put("RadioButtonMenuItem.selectionBackground", fondSelection);
        UIManager.put("RadioButtonMenuItem.selectionForeground", texteSelection);
        UIManager.put("MenuItem.acceleratorSelectionForeground", texteSelection);
        UIManager.put("CheckBoxMenuItem.acceleratorSelectionForeground", texteSelection);
        UIManager.put("RadioButtonMenuItem.acceleratorSelectionForeground", texteSelection);
        UIManager.put("PopupMenu.background", fondMenu);
        UIManager.put("PopupMenu.foreground", texteMenu);
    }

    private void stylerMenuRecursivement(JMenu menu, TokensTheme theme) {
        menu.setFont(theme.typography().label());
        menu.setBackground(theme.palette().surfaceBackground());
        menu.setForeground(theme.palette().bodyText());
        menu.setContentAreaFilled(false);
        menu.setBorderPainted(false);
        menu.setOpaque(true);
        menu.setUI(new MenuUiSansSurlignage());
        if (menu.getPopupMenu() != null) {
            menu.getPopupMenu().setBorder(new LineBorder(theme.palette().border(), 1, true));
            menu.getPopupMenu().setBackground(theme.palette().surfaceBackground());
        }
        installerStyleSelectionMenu(menu, theme);
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item == null) {
                continue;
            }
            item.setFont(theme.typography().label());
            item.setBackground(theme.palette().surfaceBackground());
            item.setForeground(theme.palette().bodyText());
            item.setOpaque(true);
            installerStyleSelectionMenu(item, theme);
            if (item instanceof JMenu sousMenu) {
                stylerMenuRecursivement(sousMenu, theme);
            }
        }
    }

    private void installerStyleSelectionMenu(AbstractButton boutonMenu, TokensTheme theme) {
        if (boutonMenu == null) {
            return;
        }
        if (!Boolean.TRUE.equals(boutonMenu.getClientProperty(CLE_STYLE_SELECTION_MENU))) {
            boutonMenu.addChangeListener(e -> actualiserStyleSelectionMenu(boutonMenu, fournisseurTheme.get()));
            boutonMenu.putClientProperty(CLE_STYLE_SELECTION_MENU, Boolean.TRUE);
        }
        actualiserStyleSelectionMenu(boutonMenu, theme);
    }

    private void actualiserStyleSelectionMenu(AbstractButton boutonMenu, TokensTheme theme) {
        if (boutonMenu == null || boutonMenu.getModel() == null || theme == null) {
            return;
        }
        boutonMenu.setBackground(theme.palette().surfaceBackground());
        boutonMenu.setForeground(theme.palette().bodyText());
    }

    private static final class MenuUiSansSurlignage extends BasicMenuUI {
        @Override
        protected void paintBackground(Graphics g, JMenuItem menuItem, Color bgColor) {
            g.setColor(menuItem.getBackground());
            g.fillRect(0, 0, menuItem.getWidth(), menuItem.getHeight());
        }
    }
}

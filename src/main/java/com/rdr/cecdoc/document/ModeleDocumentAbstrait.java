package com.rdr.cecdoc.document;

import com.rdr.cecdoc.util.ParseursDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ModeleDocumentAbstrait<T> implements ModeleDocument<T> {
    protected static final int TAILLE_TEXTE = 11;
    private static final DateTimeFormatter FORMAT_DATE = DateTimeFormatter.ofPattern("dd/MM/uuuu", Locale.ROOT);
    private static final Pattern MOTIF_PUCE = Pattern.compile("^(\\s*)([•◦▪])\\s*(.*)$");
    private static final int ESPACES_PAR_NIVEAU_PUCE = 2;

    protected static boolean aDuTexte(String valeur) {
        return valeur != null && !valeur.trim().isEmpty();
    }

    protected static String nettoyer(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }

    protected static String capFirst(String valeur) {
        if (valeur == null || valeur.isEmpty()) {
            return valeur;
        }
        if (valeur.length() == 1) {
            return valeur.toUpperCase(Locale.ROOT);
        }
        return Character.toUpperCase(valeur.charAt(0)) + valeur.substring(1);
    }

    protected static String lowerFirst(String valeur) {
        if (valeur == null || valeur.isEmpty()) {
            return valeur;
        }
        if (valeur.length() == 1) {
            return valeur.toLowerCase(Locale.ROOT);
        }
        return Character.toLowerCase(valeur.charAt(0)) + valeur.substring(1);
    }

    private static PuceDetectee detecterPuce(String texte) {
        Matcher matcher = MOTIF_PUCE.matcher(texte);
        if (!matcher.matches()) {
            return null;
        }
        String textePuce = matcher.group(3) == null ? "" : matcher.group(3).trim();
        int espacesIndentation = matcher.group(1) == null ? 0 : matcher.group(1).length();
        int niveau = Math.max(1, (espacesIndentation / ESPACES_PAR_NIVEAU_PUCE) + 1);
        return new PuceDetectee(niveau, textePuce);
    }

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras) {
        ajouterParagraphe(redactrice, alignement, texte, gras, false, TAILLE_TEXTE, false);
    }

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras, boolean italique) {
        ajouterParagraphe(redactrice, alignement, texte, gras, italique, TAILLE_TEXTE, false);
    }

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        String contenu = texte == null ? "" : texte;
        PuceDetectee puceDetectee = detecterPuce(contenu);
        if (puceDetectee != null) {
            redactrice.ajouterPuce(alignement, puceDetectee.niveau(), puceDetectee.texte(), gras, italique, taillePolice, sautPageAvant);
            return;
        }
        redactrice.ajouterParagraphe(alignement, contenu, gras, italique, taillePolice, sautPageAvant);
    }

    protected final void ajouterParagrapheVide(RedactriceDocument redactrice, AlignementTexte alignement) {
        ajouterParagraphe(redactrice, alignement, "", false);
    }

    protected final void ajouterBlocMultiligne(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras) {
        if (!aDuTexte(texte)) {
            ajouterParagraphe(redactrice, alignement, "", gras);
            return;
        }
        String[] lignes = texte.split("\\R");
        boolean aAjoute = false;
        for (String ligne : lignes) {
            String candidate = ligne == null ? "" : ligne.trim();
            if (candidate.isEmpty()) {
                continue;
            }
            ajouterParagraphe(redactrice, alignement, candidate, gras);
            aAjoute = true;
        }
        if (!aAjoute) {
            ajouterParagraphe(redactrice, alignement, "", gras);
        }
    }

    protected final void ajouterPuce(RedactriceDocument redactrice, AlignementTexte alignement, int niveau, String texte, boolean gras) {
        int profondeur = Math.max(1, niveau);
        redactrice.ajouterPuce(alignement, profondeur, texte == null ? "" : texte.trim(), gras, false, TAILLE_TEXTE, false);
    }

    protected final String dateOuAujourdhui(String texteDate) {
        if (ParseursDate.dateSaisieValide(texteDate)) {
            return texteDate.trim();
        }
        return LocalDate.now().format(FORMAT_DATE);
    }

    private record PuceDetectee(int niveau, String texte) {
    }
}

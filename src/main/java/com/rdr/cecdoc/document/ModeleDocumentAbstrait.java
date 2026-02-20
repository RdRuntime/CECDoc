package com.rdr.cecdoc.document;

import java.util.Locale;

public abstract class ModeleDocumentAbstrait<T> implements ModeleDocument<T> {
    protected static final int TAILLE_TEXTE = 11;

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras) {
        ajouterParagraphe(redactrice, alignement, texte, gras, false, TAILLE_TEXTE, false);
    }

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras, boolean italique) {
        ajouterParagraphe(redactrice, alignement, texte, gras, italique, TAILLE_TEXTE, false);
    }

    protected final void ajouterParagraphe(RedactriceDocument redactrice, AlignementTexte alignement, String texte, boolean gras, boolean italique, int taillePolice, boolean sautPageAvant) {
        redactrice.ajouterParagraphe(alignement, texte == null ? "" : texte, gras, italique, taillePolice, sautPageAvant);
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
}

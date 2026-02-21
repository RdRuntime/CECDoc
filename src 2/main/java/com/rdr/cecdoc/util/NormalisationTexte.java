package com.rdr.cecdoc.util;

import java.util.Locale;

public final class NormalisationTexte {
    private NormalisationTexte() {
    }

    public static String normaliserTexte(String valeur) {
        return valeur == null ? "" : valeur.trim();
    }

    public static String normaliserNomPropre(String valeur) {
        String texte = normaliserTexte(valeur);
        if (texte.isEmpty()) {
            return "";
        }
        if (estTexteMajuscule(texte)) {
            return texte.toUpperCase(Locale.ROOT);
        }
        StringBuilder resultat = new StringBuilder(texte.length());
        boolean majuscule = true;
        for (int i = 0; i < texte.length(); i++) {
            char caractere = texte.charAt(i);
            if (Character.isLetter(caractere)) {
                resultat.append(majuscule ? Character.toUpperCase(caractere) : Character.toLowerCase(caractere));
                majuscule = false;
            } else {
                resultat.append(caractere);
                majuscule = caractere == ' ' || caractere == '-' || caractere == '\'' || caractere == '’' || caractere == ',';
            }
        }
        return resultat.toString();
    }

    public static String extrairePremierPrenom(String prenoms) {
        String texte = normaliserTexte(prenoms);
        if (texte.isEmpty()) {
            return "";
        }
        int indexVirgule = texte.indexOf(',');
        int indexEspace = texte.indexOf(' ');
        int indexSeparateur;
        if (indexVirgule < 0) {
            indexSeparateur = indexEspace;
        } else if (indexEspace < 0) {
            indexSeparateur = indexVirgule;
        } else {
            indexSeparateur = Math.min(indexVirgule, indexEspace);
        }
        if (indexSeparateur < 0) {
            return normaliserNomPropre(texte);
        }
        return normaliserNomPropre(texte.substring(0, indexSeparateur));
    }

    public static String aplatirLignes(String texte) {
        String valeur = normaliserTexte(texte);
        if (valeur.isEmpty()) {
            return "";
        }
        return valeur.replaceAll("\\R+", " ").replaceAll("\\s{2,}", " ").trim();
    }

    public static String minusculerPourMilieuPhrase(String texte) {
        String valeur = normaliserTexte(texte);
        if (valeur.isEmpty()) {
            return "";
        }
        return valeur.toLowerCase(Locale.ROOT);
    }

    private static boolean estTexteMajuscule(String texte) {
        boolean contientLettre = false;
        for (int i = 0; i < texte.length(); i++) {
            char caractere = texte.charAt(i);
            if (!Character.isLetter(caractere)) {
                continue;
            }
            contientLettre = true;
            if (Character.isLowerCase(caractere)) {
                return false;
            }
        }
        return contientLettre;
    }
}

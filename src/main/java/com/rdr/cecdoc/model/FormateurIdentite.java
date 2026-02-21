package com.rdr.cecdoc.model;

import com.rdr.cecdoc.util.NormalisationTexte;

import java.util.Locale;
import java.util.Objects;

public final class FormateurIdentite {
    private FormateurIdentite() {
    }

    public static String assemblerPrenomsNom(String prenoms, String nom) {
        String prenomsNettoyes = NormalisationTexte.normaliserTexte(prenoms);
        String nomNettoye = normaliserNomMajuscules(nom);
        if (prenomsNettoyes.isBlank()) {
            return nomNettoye;
        }
        if (nomNettoye.isBlank()) {
            return prenomsNettoyes;
        }
        return prenomsNettoyes + " " + nomNettoye;
    }

    public static String identiteAvecEtatCivil(String prenomsUsage, String prenomsEtatCivil, String nom) {
        String nomNettoye = normaliserNomMajuscules(nom);
        String usage = NormalisationTexte.normaliserTexte(prenomsUsage);
        String etatCivil = NormalisationTexte.normaliserTexte(prenomsEtatCivil);
        if (usage.isBlank()) {
            usage = etatCivil;
        }
        if (etatCivil.isBlank()) {
            etatCivil = usage;
        }
        String identiteUsage = assemblerPrenomsNom(usage, nomNettoye);
        String identiteEtatCivil = assemblerPrenomsNom(etatCivil, nomNettoye);
        if (identiteUsage.isBlank()) {
            return identiteEtatCivil;
        }
        if (identiteEtatCivil.isBlank() || prenomsEquivalents(usage, etatCivil)) {
            return identiteUsage;
        }
        return identiteUsage + " (" + identiteEtatCivil + " pour l'état civil)";
    }

    private static String normaliserNomMajuscules(String nom) {
        String nomNettoye = NormalisationTexte.normaliserTexte(nom);
        if (nomNettoye.isBlank()) {
            return "";
        }
        return nomNettoye.toUpperCase(Locale.ROOT);
    }

    private static boolean prenomsEquivalents(String premier, String second) {
        return Objects.equals(normaliserPrenomsPourComparaison(premier), normaliserPrenomsPourComparaison(second));
    }

    private static String normaliserPrenomsPourComparaison(String prenoms) {
        String nettoye = NormalisationTexte.normaliserTexte(prenoms).toLowerCase(Locale.ROOT);
        return nettoye.replace(",", " ").replaceAll("\\s+", " ").trim();
    }
}

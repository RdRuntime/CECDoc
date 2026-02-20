package com.rdr.cecdoc.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public final class ServiceMiseAJour {
    private static final URI URI_VERSION_DISTANTE = URI.create("https://github.com/RdRuntime/CECDoc/raw/refs/heads/main/version");
    private static final URI URI_PAGE_PROJET = URI.create("https://github.com/RdRuntime/CECDoc");
    private static final Duration DELAI_CONNEXION = Duration.ofSeconds(8);
    private static final Duration DELAI_REQUETE = Duration.ofSeconds(10);
    private final HttpClient clienteHttp;

    public ServiceMiseAJour() {
        this(HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(DELAI_CONNEXION).build());
    }

    ServiceMiseAJour(HttpClient clienteHttp) {
        this.clienteHttp = clienteHttp;
    }

    public ResultatVerificationMiseAJour verifier(String versionLocale) throws IOException, InterruptedException {
        String versionCourante = normaliserVersion(versionLocale);
        HttpRequest requete = HttpRequest.newBuilder().uri(URI_VERSION_DISTANTE).timeout(DELAI_REQUETE).GET().build();
        HttpResponse<String> reponse = clienteHttp.send(requete, HttpResponse.BodyHandlers.ofString());

        if (reponse.statusCode() != 200) {
            throw new IOException("Réponse HTTP inattendue : " + reponse.statusCode());
        }

        String versionDistante = normaliserVersion(reponse.body());
        if (versionDistante.isBlank() || !versionValide(versionDistante)) {
            throw new IOException("Numéro de version distant invalide.");
        }

        boolean miseAJourDisponible = comparerVersions(versionDistante, versionCourante) > 0;
        return new ResultatVerificationMiseAJour(versionCourante, versionDistante, miseAJourDisponible);
    }

    public URI uriProjet() {
        return URI_PAGE_PROJET;
    }

    public String versionLocaleDepuisClasspath(Class<?> classeReference) {
        if (classeReference == null) {
            return "inconnue";
        }
        Package paquet = classeReference.getPackage();
        if (paquet != null) {
            String implementationVersion = normaliserVersion(paquet.getImplementationVersion());
            if (!implementationVersion.isBlank() && versionValide(implementationVersion)) {
                return implementationVersion;
            }
        }

        try (InputStream flux = classeReference.getResourceAsStream("/META-INF/maven/com.rdr/CECDoc/pom.properties")) {
            if (flux == null) {
                return versionLocaleDepuisPomLocal();
            }
            Properties proprietes = new Properties();
            proprietes.load(flux);
            String version = normaliserVersion(proprietes.getProperty("version"));
            if (!version.isBlank() && versionValide(version)) {
                return version;
            }
        } catch (IOException ignored) {
            return versionLocaleDepuisPomLocal();
        }
        return versionLocaleDepuisPomLocal();
    }

    static int comparerVersions(String versionA, String versionB) {
        String[] morceauxA = normaliserVersion(versionA).split("\\.");
        String[] morceauxB = normaliserVersion(versionB).split("\\.");
        int longueur = Math.max(morceauxA.length, morceauxB.length);
        for (int i = 0; i < longueur; i++) {
            int valeurA = i < morceauxA.length ? convertirMorceauVersion(morceauxA[i]) : 0;
            int valeurB = i < morceauxB.length ? convertirMorceauVersion(morceauxB[i]) : 0;
            if (valeurA != valeurB) {
                return Integer.compare(valeurA, valeurB);
            }
        }
        return 0;
    }

    static boolean versionValide(String version) {
        return normaliserVersion(version).matches("^\\d+(?:\\.\\d+)*$");
    }

    private static int convertirMorceauVersion(String morceau) {
        String texte = normaliserVersion(morceau);
        if (texte.isBlank()) {
            return 0;
        }
        try {
            return Integer.parseInt(texte);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    private static String normaliserVersion(String version) {
        return version == null ? "" : version.trim().toLowerCase(Locale.ROOT);
    }

    private static String versionLocaleDepuisPomLocal() {
        Path pom = Path.of("pom.xml").toAbsolutePath().normalize();
        if (!Files.isRegularFile(pom)) {
            return "inconnue";
        }
        try {
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            fabrique.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            fabrique.setFeature("http://xml.org/sax/features/external-general-entities", false);
            fabrique.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            fabrique.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            fabrique.setExpandEntityReferences(false);
            fabrique.setNamespaceAware(false);
            fabrique.setXIncludeAware(false);
            Document document = fabrique.newDocumentBuilder().parse(pom.toFile());
            NodeList racineProjet = document.getElementsByTagName("project");
            if (racineProjet.getLength() > 0 && racineProjet.item(0) instanceof org.w3c.dom.Element elementProjet) {
                NodeList enfants = elementProjet.getElementsByTagName("version");
                if (enfants.getLength() > 0) {
                    String version = normaliserVersion(enfants.item(0).getTextContent());
                    if (!version.isBlank() && versionValide(version)) {
                        return version;
                    }
                }
            }
        } catch (Exception ignored) {
            return "inconnue";
        }
        return "inconnue";
    }

    public record ResultatVerificationMiseAJour(String versionLocale, String versionDistante, boolean miseAJourDisponible) {
    }
}

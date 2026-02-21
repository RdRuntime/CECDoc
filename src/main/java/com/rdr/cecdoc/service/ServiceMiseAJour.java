package com.rdr.cecdoc.service;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Locale;
import java.util.Properties;

public final class ServiceMiseAJour {
    private static final System.Logger JOURNAL = System.getLogger(ServiceMiseAJour.class.getName());
    private static final URI URI_VERSION_DISTANTE = URI.create("https://github.com/RdRuntime/CECDoc/raw/refs/heads/main/version");
    private static final URI URI_PAGE_PROJET = URI.create("https://github.com/RdRuntime/CECDoc");
    private static final String FONCTIONNALITE_XML_INTERDIRE_DOCTYPE = "http://apache.org/xml/features/disallow-doctype-decl";
    private static final String FONCTIONNALITE_XML_ENTITES_GENERALES_EXTERNES = "http://xml.org/sax/features/external-general-entities";
    private static final String FONCTIONNALITE_XML_ENTITES_PARAMETRES_EXTERNES = "http://xml.org/sax/features/external-parameter-entities";
    private static final String FONCTIONNALITE_XML_DTD_EXTERNE = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
    private static final int LONGUEUR_MAX_VERSION_DISTANTE = 32;
    private static final Duration DELAI_CONNEXION = Duration.ofSeconds(8);
    private static final Duration DELAI_REQUETE = Duration.ofSeconds(10);
    private final HttpClient clienteHttp;

    public ServiceMiseAJour() {
        this(HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(DELAI_CONNEXION).build());
    }

    ServiceMiseAJour(HttpClient clienteHttp) {
        this.clienteHttp = clienteHttp;
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
        String versionNormalisee = normaliserVersion(version);
        if (versionNormalisee.isBlank()) {
            return false;
        }
        int debut = 0;
        while (debut < versionNormalisee.length()) {
            int indexPoint = versionNormalisee.indexOf('.', debut);
            int fin = indexPoint < 0 ? versionNormalisee.length() : indexPoint;
            if (fin <= debut || !estMorceauNumerique(versionNormalisee, debut, fin)) {
                return false;
            }
            if (indexPoint < 0) {
                return true;
            }
            debut = indexPoint + 1;
        }
        return false;
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

    private static boolean estMorceauNumerique(String texte, int debut, int fin) {
        for (int i = debut; i < fin; i++) {
            if (!Character.isDigit(texte.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private static String versionLocaleDepuisPomLocal() {
        Path pom = Path.of("pom.xml").toAbsolutePath().normalize();
        if (!Files.isRegularFile(pom)) {
            return "inconnue";
        }
        try {
            DocumentBuilderFactory fabrique = DocumentBuilderFactory.newInstance();
            fabrique.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            definirFonctionnaliteXmlAvecRepliHttp(fabrique, FONCTIONNALITE_XML_INTERDIRE_DOCTYPE, true);
            definirFonctionnaliteXmlAvecRepliHttp(fabrique, FONCTIONNALITE_XML_ENTITES_GENERALES_EXTERNES, false);
            definirFonctionnaliteXmlAvecRepliHttp(fabrique, FONCTIONNALITE_XML_ENTITES_PARAMETRES_EXTERNES, false);
            definirFonctionnaliteXmlAvecRepliHttp(fabrique, FONCTIONNALITE_XML_DTD_EXTERNE, false);
            definirAttributXmlSiSupporte(fabrique, XMLConstants.ACCESS_EXTERNAL_DTD, "");
            definirAttributXmlSiSupporte(fabrique, XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
            fabrique.setExpandEntityReferences(false);
            fabrique.setNamespaceAware(false);
            fabrique.setXIncludeAware(false);
            var constructeur = fabrique.newDocumentBuilder();
            EntityResolver resolveurLocal = (publicId, systemId) -> new InputSource(new StringReader(""));
            constructeur.setEntityResolver(resolveurLocal);
            Document document = constructeur.parse(pom.toFile());
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
        } catch (ParserConfigurationException | SAXException | IOException | RuntimeException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Lecture locale de version depuis pom.xml impossible.", ex);
            return "inconnue";
        }
        return "inconnue";
    }

    private static void definirAttributXmlSiSupporte(DocumentBuilderFactory fabrique, String cle, String valeur) {
        try {
            fabrique.setAttribute(cle, valeur);
        } catch (IllegalArgumentException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Attribut XML non supporté : " + cle, ex);
        }
    }

    private static void definirFonctionnaliteXmlAvecRepliHttp(DocumentBuilderFactory fabrique, String cleHttp, boolean valeur) throws ParserConfigurationException {
        String cleHttps = cleHttp.replaceFirst("^http://", "https://");
        try {
            fabrique.setFeature(cleHttps, valeur);
        } catch (ParserConfigurationException ex) {
            fabrique.setFeature(cleHttp, valeur);
        }
    }

    public ResultatVerificationMiseAJour verifier(String versionLocale) throws IOException, InterruptedException {
        String versionCourante = normaliserVersion(versionLocale);
        HttpRequest requete = HttpRequest.newBuilder().uri(URI_VERSION_DISTANTE).timeout(DELAI_REQUETE).GET().build();
        HttpResponse<String> reponse = clienteHttp.send(requete, HttpResponse.BodyHandlers.ofString());

        if (reponse.statusCode() != 200) {
            throw new IOException("Réponse HTTP inattendue : " + reponse.statusCode());
        }
        URI uriFinale = reponse.uri();
        if (uriFinale == null || !"https".equalsIgnoreCase(uriFinale.getScheme())) {
            throw new IOException("Réponse distante invalide.");
        }
        String hoteFinal = uriFinale.getHost() == null ? "" : uriFinale.getHost().toLowerCase(Locale.ROOT);
        boolean hoteGithub = "github.com".equals(hoteFinal) || hoteFinal.endsWith(".github.com");
        boolean hoteGithubusercontent = "githubusercontent.com".equals(hoteFinal) || hoteFinal.endsWith(".githubusercontent.com");
        if (!hoteGithub && !hoteGithubusercontent) {
            throw new IOException("Source distante non autorisée.");
        }

        String versionDistante = normaliserVersion(reponse.body());
        if (versionDistante.length() > LONGUEUR_MAX_VERSION_DISTANTE) {
            throw new IOException("Numéro de version distant invalide.");
        }
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
        } catch (IOException ex) {
            JOURNAL.log(System.Logger.Level.DEBUG, "Lecture de version depuis le classpath impossible.", ex);
            return versionLocaleDepuisPomLocal();
        }
        return versionLocaleDepuisPomLocal();
    }

    public record ResultatVerificationMiseAJour(String versionLocale, String versionDistante,
                                                boolean miseAJourDisponible) {
    }
}

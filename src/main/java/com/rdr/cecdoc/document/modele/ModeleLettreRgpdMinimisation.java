package com.rdr.cecdoc.document.modele;

import com.rdr.cecdoc.document.AlignementTexte;
import com.rdr.cecdoc.document.ModeleDocumentAbstrait;
import com.rdr.cecdoc.document.RedactriceDocument;
import com.rdr.cecdoc.model.DonneesLettreRgpdMinimisation;

import java.util.Objects;

public final class ModeleLettreRgpdMinimisation extends ModeleDocumentAbstrait<DonneesLettreRgpdMinimisation> {
    @Override
    public void rediger(DonneesLettreRgpdMinimisation donnees, RedactriceDocument redactrice) {
        Objects.requireNonNull(donnees, "donnees");
        Objects.requireNonNull(redactrice, "redactrice");

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.GAUCHE, donnees.adressePostale(), false);
        if (aDuTexte(donnees.ligneContact())) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.ligneContact(), false);
        }
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À l’attention du/de la Délégué·e à la protection des données (DPO) / Service conformité", false);
        ajouterBlocMultiligne(redactrice, AlignementTexte.DROITE, donnees.nomAdresseOrganisme(), false);

        ajouterParagraphe(redactrice, AlignementTexte.DROITE, "À " + donnees.villeRedaction() + ", le " + dateOuAujourdhui(donnees.dateRedaction()), false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);

        ajouterParagraphe(redactrice, AlignementTexte.CENTRE, "Objet : Demande de minimisation, de limitation du traitement et de sécurisation des données d’identification – exercice de droits RGPD (articles 5, 6, 9, 12, 13, 15 à 19, 21, 24, 25, 30, 32)", true);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Madame, Monsieur,", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je, " + donnees.adjectifSousSigne() + " " + donnees.identiteEntete() + ", " + donnees.adjectifNe() + " le " + dateOuAujourdhui(donnees.dateNaissance()) + " à " + donnees.lieuNaissance() + ", vous adresse la présente demande afin de faire valoir mes droits relatifs à la protection de mes données à caractère personnel, conformément au Règlement (UE) 2016/679 (RGPD).", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La présente demande porte spécifiquement sur les données d’identification et leurs usages dans vos systèmes, notamment : prénoms, noms, civilité, genre/mention d’affichage, identifiants internes, historique d’identité, alias, champs commentaires, pièces justificatives associées, ainsi que sur les modalités d’affichage et de diffusion interne de ces informations.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1. Rappel du cadre juridique applicable", true);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.1 Principes de traitement (article 5)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 5 du RGPD, vos traitements doivent respecter :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la licéité, loyauté et transparence ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la limitation des finalités ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la minimisation des données (données adéquates, pertinentes et limitées à ce qui est nécessaire) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "l’exactitude ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la limitation de la conservation ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "l’intégrité et la confidentialité.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Vous devez en outre être en mesure de démontrer le respect de ces principes (responsabilité – accountability).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.2 Licéité et base légale (article 6)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 6 du RGPD, tout traitement doit reposer sur une base légale identifiable (obligation légale, exécution d’un contrat, mission d’intérêt public, intérêt légitime, etc.). En conséquence, chaque champ collecté/affiché (civilité, mention de genre, historique d’identité, etc.) doit être rattaché à une finalité déterminée et à une base légale précise.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.3 Données sensibles (article 9)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 9 du RGPD, le traitement de catégories particulières de données est en principe interdit sauf exceptions prévues par le texte. Selon la manière dont vos outils sont paramétrés, certains champs (mentions relatives au sexe/genre, éléments de parcours, justificatifs) peuvent révéler indirectement des informations particulièrement sensibles. J’attends donc une vigilance renforcée sur le strict nécessaire, l’accès restreint et la sécurité.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.4 Modalités d’exercice des droits (article 12)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 12 du RGPD, vous devez :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "faciliter l’exercice de mes droits ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "me répondre sans retard injustifié et au plus tard dans le délai d’un mois à compter de la réception de la demande (prorogation possible dans les conditions strictes prévues) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "fournir une réponse claire, motivée et compréhensible.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.5 Information et transparence (article 13)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "En vertu de l’article 13 du RGPD (données collectées auprès de la personne concernée), vous devez m’informer notamment de : finalités, base légale, destinataires/catégories de destinataires, durée de conservation, existence de mes droits et modalités pratiques.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.6 Droits mobilisés (articles 15 à 19 et 21)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "La présente demande met en œuvre :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "le droit d’accès (article 15),", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "le droit de rectification (article 16),", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "le droit à l’effacement (article 17) dans la mesure pertinente,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "le droit à la limitation du traitement (article 18),", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "l’obligation de notification aux destinataires en cas de rectification/effacement/limitation (article 19),", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "le droit d’opposition (article 21) lorsque le traitement est fondé sur l’intérêt légitime ou une mission d’intérêt public.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1.7 Responsabilité, conception et sécurité (articles 24, 25, 30, 32)", true);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Article 24", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 2, "vous devez mettre en œuvre des mesures techniques et organisationnelles appropriées et pouvoir démontrer la conformité.", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Article 25", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 2, "protection des données dès la conception et par défaut : par défaut, seuls les champs nécessaires doivent être collectés et accessibles ; les paramètres doivent limiter l’affichage et les accès.", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Article 30", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 2, "tenue d’un registre des activités de traitement (finalités, catégories, destinataires, durées de conservation, mesures de sécurité).", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Article 32", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 2, "sécurité adaptée au risque (confidentialité, intégrité, disponibilité), contrôle d’accès, traçabilité, procédures.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2. Exposé des difficultés et enjeux de minimisation/confidentialité", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je constate/anticipe que certains de vos systèmes et usages (interfaces, courriers, espaces clients, listes, tickets, écrans d’accueil, e-mails automatiques, historiques internes) sont susceptibles :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "d’afficher et/ou de diffuser au-delà du strict nécessaire des informations d’identification,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "de conserver des champs d’historique (anciens prénoms, ancienne civilité, anciennes mentions) accessibles à des personnes non habilitées,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "de créer un risque de divulgation non souhaitée (outing) et d’atteinte à ma vie privée.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Ces risques imposent une application rigoureuse des principes de minimisation, de limitation des finalités, d’exactitude, de privacy by default et de sécurité.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3. Demandes précises", true);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.1 Accès et cartographie de mes données (article 15)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande communication :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) de la liste exhaustive des données d’identification me concernant présentes dans vos systèmes (y compris champs masqués, historiques, commentaires, logs applicatifs liés au profil, alias, champs techniques) ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) des finalités et de la base légale associées à chaque catégorie de données (article 6) ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3) des destinataires ou catégories de destinataires internes (services) ayant accès à ces données, et des règles d’habilitation ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4) des durées de conservation applicables aux champs d’identification et aux historiques ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "5) d’une copie des données d’identification (export lisible), incluant les historiques s’ils existent.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.2 Rectification (article 16)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande la rectification et l’usage effectif, pour les affichages et communications d’usage courant, des mentions suivantes :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Prénoms changés à l’état-civil : " + donnees.prenomsEtatCivil(), false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Nom : " + donnees.nomMajuscules(), false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Sexe à l’état civil : " + donnees.sexeEtatCivil(), false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "Civilité/mention d’affichage : " + donnees.civiliteAffichage(), false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande également la suppression/correction de toute donnée inexacte, incohérente ou obsolète.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.3 Minimisation et by default (article 5(1)(c) et article 25)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande la mise en conformité de vos paramétrages et procédures afin que :", false);
        if (donnees.champsCiviliteGenrePresents()) {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) les champs civilité, genre, mention d’affichage soient facultatifs ou neutralisables lorsqu’ils ne sont pas strictement nécessaires ;", false);
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) les interfaces, courriers et automatisations n’affichent que les champs nécessaires à la finalité (pas d’historique, pas d’ancienne mention, pas de doublons) ;", false);
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3) les écrans/exports à large diffusion (équipes front office, prestataires internes, support de niveau 1) n’exposent pas d’historique d’identité.", false);
        } else {
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) les interfaces, courriers et automatisations n’affichent que les champs nécessaires à la finalité (pas d’historique, pas d’ancienne mention, pas de doublons) ;", false);
            ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) les écrans/exports à large diffusion (équipes front office, prestataires internes, support de niveau 1) n’exposent pas d’historique d’identité.", false);
        }
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande que vous me précisiez les mesures techniques prises au titre de l’article 25 (paramètres par défaut, masquage, rôles, profils, suppression d’affichages superflus).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.4 Limitation du traitement des anciennes mentions (article 18)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Lorsque des anciennes mentions d’identité existent ou doivent être conservées pour une raison strictement justifiée, je demande la limitation du traitement au sens de l’article 18 :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "conservation dans un espace restreint,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "accès réservé à un nombre strictement limité de personnes habilitées,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "interdiction d’usage dans les affichages, courriers, exports, tickets, e-mails automatiques,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "traçabilité des accès,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "et information préalable avant toute levée de limitation.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.5 Effacement des champs non nécessaires (article 17, dans la mesure applicable)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande l’effacement de tout champ/annotation/historique non nécessaire aux finalités actuelles et non imposé par une obligation légale :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "champs commentaires contenant des informations sensibles ou superflues,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "copies de justificatifs conservées au-delà de ce qui est nécessaire,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "champs historiques sans finalité et sans base légale.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Si vous estimez qu’une partie ne peut pas être effacée, je vous demande une motivation précise (finalité, base légale, durée de conservation) et, à défaut d’effacement, la limitation (article 18).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.6 Notification aux destinataires (article 19)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous demande de notifier toute rectification, effacement ou limitation aux destinataires auxquels ces données auraient été communiquées, conformément à l’article 19, et de me confirmer l’exécution de cette notification (ou l’impossibilité dûment justifiée).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.7 Opposition (article 21) – traitements fondés sur l’intérêt légitime", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Dans la mesure où certains traitements d’identification (affichage de civilité, enrichissement de profil, historisation non nécessaire, usages confort) seraient fondés sur l’intérêt légitime, je m’y oppose (article 21) dès lors qu’ils ne sont pas strictement nécessaires et qu’ils portent atteinte à mes droits et libertés. Je vous demande de cesser ces traitements, sauf démonstration de motifs légitimes impérieux.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.8 Sécurisation et contrôle d’accès (articles 5(1)(f) et 32)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande la mise en œuvre (ou la confirmation) de mesures de sécurité adaptées au risque, notamment :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "contrôle d’accès par rôles (moindre privilège),", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "journalisation/traçabilité des accès aux champs sensibles,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "procédures internes de confidentialité,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "limitation des exports et impressions,", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "dispositifs anti-divulgation dans les processus front-office.", false);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous demande de me confirmer, de manière non technique mais précise, les mesures organisationnelles et techniques pertinentes mises en place.", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3.9 Documentation de conformité (articles 24 et 30)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je demande :", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "l’identification du responsable du traitement et, le cas échéant, les coordonnées du/de la DPO ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la confirmation que vos traitements sont couverts par un registre conforme à l’article 30 pour la partie me concernant (finalités, catégories, durées, mesures de sécurité) ;", false);
        ajouterPuce(redactrice, AlignementTexte.GAUCHE, 1, "la confirmation des mesures prises au titre de l’article 24 (capacité à démontrer la conformité).", false);

        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "4. Modalités pratiques et délai de réponse (article 12)", true);
        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous remercie :", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "1) d’accuser réception de la présente demande ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "2) de me répondre par écrit (courrier ou e-mail) dans un délai d’un mois à compter de sa réception, conformément à l’article 12 du RGPD ;", false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "3) en cas de refus total ou partiel, de motiver précisément votre décision (finalités, base légale, durée de conservation, mesures alternatives), et de préciser les voies de recours.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "À défaut de réponse dans les délais ou en cas de réponse insuffisante, je me réserve la possibilité de saisir l’autorité de contrôle compétente.", false);

        ajouterParagraphe(redactrice, AlignementTexte.JUSTIFIE, "Je vous prie d’agréer, Madame, Monsieur, l’expression de ma considération distinguée.", false);
        ajouterParagrapheVide(redactrice, AlignementTexte.GAUCHE);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, donnees.identiteEntete(), false);
        ajouterParagraphe(redactrice, AlignementTexte.GAUCHE, "Signature", false);
    }
}

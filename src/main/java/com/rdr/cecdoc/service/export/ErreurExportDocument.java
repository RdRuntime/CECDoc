package com.rdr.cecdoc.service.export;

public final class ErreurExportDocument extends Exception {

    private final Reason reason;
    private final String userMessage;

    private ErreurExportDocument(Reason reason, String userMessage, Throwable cause) {
        super(userMessage, cause);
        this.reason = reason;
        this.userMessage = userMessage;
    }

    public static ErreurExportDocument fileAlreadyExists() {
        return new ErreurExportDocument(Reason.FILE_ALREADY_EXISTS, "Un document avec ce nom existe déjà. Choisissez un autre nom ou confirmez l'écrasement.", null);
    }

    public static ErreurExportDocument permissionDenied(Throwable cause) {
        return new ErreurExportDocument(Reason.PERMISSION_DENIED, "Impossible d'écrire le document à cet emplacement (droits insuffisants).", cause);
    }

    public static ErreurExportDocument fileLockedOrUnavailable(Throwable cause) {
        return new ErreurExportDocument(Reason.FILE_LOCKED_OR_UNAVAILABLE, "Impossible d'écrire le document. Vérifiez qu'il n'est pas ouvert dans une autre application.", cause);
    }

    public static ErreurExportDocument atomicMoveUnsupported(Throwable cause) {
        return new ErreurExportDocument(Reason.ATOMIC_MOVE_UNSUPPORTED, "Impossible de finaliser l'enregistrement de manière atomique à cet emplacement.", cause);
    }

    public static ErreurExportDocument ioFailure(Throwable cause) {
        return new ErreurExportDocument(Reason.IO_FAILURE, "Une erreur est survenue pendant l'écriture du document.", cause);
    }

    public static ErreurExportDocument wordConversionDependencyMissing(String fileName, Throwable cause) {
        String nom = fileName == null || fileName.isBlank() ? "document Word" : fileName;
        return new ErreurExportDocument(Reason.WORD_TO_PDF_DEPENDENCY_MISSING, "Impossible de convertir en PDF le fichier « " + nom + " ». Vérifiez qu'une installation LibreOffice est disponible sur cette machine.", cause);
    }

    public static ErreurExportDocument wordConversionDirectFailed(String fileName, Throwable cause) {
        String nom = fileName == null || fileName.isBlank() ? "document Word" : fileName;
        return new ErreurExportDocument(Reason.WORD_TO_PDF_DIRECT_FAILURE, "Impossible de convertir directement en PDF le fichier « " + nom + " ». Vérifiez que le DOCX est valide.", cause);
    }

    public static ErreurExportDocument attachmentMissing(String fileName) {
        String nom = fileName == null || fileName.isBlank() ? "fichier joint" : fileName;
        return new ErreurExportDocument(Reason.ATTACHMENT_MISSING, "Le fichier joint « " + nom + " » est introuvable. Retirez-le ou remplacez-le dans les pièces justificatives.", null);
    }

    public static ErreurExportDocument attachmentInvalidPath(String fileName) {
        String nom = fileName == null || fileName.isBlank() ? "fichier joint" : fileName;
        return new ErreurExportDocument(Reason.ATTACHMENT_INVALID_PATH, "Le chemin du fichier joint « " + nom + " » est invalide. Retirez-le ou remplacez-le.", null);
    }

    public static ErreurExportDocument attachmentUnsupportedType(String fileName) {
        String nom = fileName == null || fileName.isBlank() ? "fichier joint" : fileName;
        return new ErreurExportDocument(Reason.ATTACHMENT_UNSUPPORTED_TYPE, "Le type du fichier joint « " + nom + " » n'est pas pris en charge. Formats acceptés: DOC, DOCX, ODT, PDF, JPG, JPEG, PNG.", null);
    }

    public static ErreurExportDocument attachmentConversionFailed(String fileName, Throwable cause) {
        String nom = fileName == null || fileName.isBlank() ? "fichier joint" : fileName;
        return new ErreurExportDocument(Reason.ATTACHMENT_CONVERSION_FAILED, "Impossible de convertir le fichier joint « " + nom + " » en PDF. Vérifiez que le fichier n'est pas corrompu ou protégé.", cause);
    }

    public static ErreurExportDocument pdfMergeFailure(Throwable cause) {
        return new ErreurExportDocument(Reason.PDF_MERGE_FAILURE, "Impossible de finaliser le PDF du dossier complet.", cause);
    }

    public Reason getReason() {
        return reason;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public enum Reason {
        FILE_ALREADY_EXISTS, PERMISSION_DENIED, FILE_LOCKED_OR_UNAVAILABLE, ATOMIC_MOVE_UNSUPPORTED, IO_FAILURE, WORD_TO_PDF_DEPENDENCY_MISSING, WORD_TO_PDF_DIRECT_FAILURE, ATTACHMENT_MISSING, ATTACHMENT_INVALID_PATH, ATTACHMENT_UNSUPPORTED_TYPE, ATTACHMENT_CONVERSION_FAILED, PDF_MERGE_FAILURE
    }
}

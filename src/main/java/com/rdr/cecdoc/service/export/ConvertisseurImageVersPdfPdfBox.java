package com.rdr.cecdoc.service.export;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

final class ConvertisseurImageVersPdfPdfBox implements ServicePdfDossierComplet.ConvertisseurImageVersPdf {
    private static final float MARGE = 36f;

    @Override
    public void convertir(Path fichierSource, Path fichierDestination, String nomFichier) throws ErreurExportDocument {
        try {
            BufferedImage image = ImageIO.read(fichierSource.toFile());
            if (image == null) {
                throw new IOException("Format d'image non pris en charge");
            }

            PDRectangle formatPage = image.getWidth() >= image.getHeight() ? new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()) : PDRectangle.A4;
            try (PDDocument document = new PDDocument()) {
                PDPage page = new PDPage(formatPage);
                document.addPage(page);

                PDImageXObject imagePdf = LosslessFactory.createFromImage(document, image);
                float largeurPage = formatPage.getWidth();
                float hauteurPage = formatPage.getHeight();
                float largeurMax = largeurPage - (MARGE * 2f);
                float hauteurMax = hauteurPage - (MARGE * 2f);
                float ratioLargeur = largeurMax / image.getWidth();
                float ratioHauteur = hauteurMax / image.getHeight();
                float ratio = Math.min(ratioLargeur, ratioHauteur);
                float largeurDessin = image.getWidth() * ratio;
                float hauteurDessin = image.getHeight() * ratio;
                float positionX = (largeurPage - largeurDessin) / 2f;
                float positionY = (hauteurPage - hauteurDessin) / 2f;

                try (PDPageContentStream contenu = new PDPageContentStream(document, page)) {
                    contenu.drawImage(imagePdf, positionX, positionY, largeurDessin, hauteurDessin);
                }
                document.save(fichierDestination.toFile());
            }
        } catch (IOException ex) {
            throw ErreurExportDocument.attachmentConversionFailed(nomFichier, ex);
        }
    }
}

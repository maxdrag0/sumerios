package com.mad.sumerios.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class EncabezadoExpensas extends PdfPageEventHelper {

    private final Image logo;
    private final float offsetY;
    private final Font titleFont;
    private final Font subtitleFont;
    private final Font boldFontBigTitle;
    private final Font boldFontTitle;
    private final BaseColor verdeAzulado;
    private final BaseColor grayBackground;
    private final String periodoFormateado;
    private final AdministracionResponseDTO adm;
    private final ConsorcioResponseDTO consorcio;


    public EncabezadoExpensas(
            float ancho, float alto, float offsetY,
            Font titleFont, Font subtitleFont, Font boldFontBigTitle,
                              Font boldFontTitle, BaseColor verdeAzulado, BaseColor grayBackground,
                              String periodoFormateado, AdministracionResponseDTO adm, ConsorcioResponseDTO consorcio) throws Exception {
        this.titleFont = titleFont;
        this.subtitleFont = subtitleFont;
        this.boldFontBigTitle = boldFontBigTitle;
        this.boldFontTitle = boldFontTitle;
        this.verdeAzulado = verdeAzulado;
        this.grayBackground = grayBackground;
        this.periodoFormateado = periodoFormateado;
        this.adm = adm;
        this.consorcio = consorcio;
        this.offsetY = offsetY;

        try (InputStream logoStream = getClass().getResourceAsStream("/static/images/sumeriosOp.png")) {
            if (logoStream == null) {
                throw new Exception("No se encontró el logo en la ruta especificada.");
            }
            this.logo = Image.getInstance(IOUtils.toByteArray(logoStream));
            this.logo.scaleToFit(ancho, alto);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
            // ======== DIBUJAR LOGO ========
            PdfContentByte cb = writer.getDirectContent();
            float x = (document.getPageSize().getWidth() - logo.getScaledWidth()) / 2;
            float y = document.getPageSize().getHeight() - logo.getScaledHeight() - offsetY;
            logo.setAbsolutePosition(x, y);
            cb.addImage(logo);


            // ======== DIBUJAR ENCABEZADO ========
            PdfPTable titulo = new PdfPTable(1);
            titulo.setWidthPercentage(100);
            titulo.getDefaultCell().setBorder(PdfPCell.BOX);
            titulo.setSpacingBefore(5f);

            PdfPCell titleCell = new PdfPCell(new Phrase("EXPENSAS LEY 14.701", subtitleFont));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setBackgroundColor(verdeAzulado);
            titleCell.setBorder(PdfPCell.NO_BORDER);
            titleCell.setPaddingTop(15f);
            titulo.addCell(titleCell);

            PdfPCell subtitleCell = new PdfPCell(new Phrase("Liquidación de: " + periodoFormateado + "\n ", titleFont));
            subtitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            subtitleCell.setBackgroundColor(verdeAzulado);
            subtitleCell.setBorder(PdfPCell.NO_BORDER);
            titulo.addCell(subtitleCell);

            titulo.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            titulo.setLockedWidth(true);
            titulo.writeSelectedRows(
                    0, -1,
                    document.leftMargin(),
                    document.getPageSize().getTop() - 36, // altura fija desde arriba de la página
                    cb
            );

            PdfPTable encabezado = new PdfPTable(2);
            encabezado.setWidthPercentage(100);
            encabezado.setWidths(new int[]{50, 50});

            encabezado.addCell(createCell("ADMINISTRACION", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,30f));
            encabezado.addCell(createCell("CONSORCIO", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,30f));
            encabezado.addCell(createCell("Nombre: " + adm.getNombre(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Nombre: " + consorcio.getNombre(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Administrador: " + adm.getAdministrador().getNombre() + " " + adm.getAdministrador().getApellido(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Direccion: " + consorcio.getDireccion(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Direccion: " + adm.getDireccion(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("C.U.I.T.: " + consorcio.getCuit(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Telefono: " + adm.getTelefono(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("Mail: " + adm.getMail(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,15f));
            encabezado.addCell(createCell("C.U.I.T.: " + adm.getCuit(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));
            encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground,20f));

            encabezado.setTotalWidth(document.getPageSize().getWidth() - document.leftMargin() - document.rightMargin());
            encabezado.setLockedWidth(true);
            encabezado.writeSelectedRows(
                    0, -1,
                    document.leftMargin(),
                    document.getPageSize().getTop() - 105, // altura fija desde arriba de la página
                    cb
            );
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private PdfPCell createCell(String content, Font font, int alignment, int border, BaseColor backgroundColor, Float minHeight) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment); // izquierda, centro, derecha
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // centrado vertical
        cell.setBorder(border);
        cell.setPadding(5f);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minHeight != null) {
            cell.setMinimumHeight(minHeight);
        }
        return cell;
    }
}

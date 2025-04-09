package com.mad.sumerios.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import org.apache.pdfbox.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class PdfGenerator2 {

    public static void createPdfPago(PagoUFDTO pagoUF, double totalPago, AdministracionResponseDTO admDto,
                                     ConsorcioResponseDTO consorcioDto, UnidadFuncionalResponseDTO uf,
                                     String outputPath) {
        Document document = new Document(new Rectangle(320, 567), 20, 20, 20, 20);

        try {
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            document.open();

            // Agregar marca de agua centrada y transparente
//            String rutaImagen = "C:/Users/Max/Desktop/electron/sumerios/src/renderer/src/assets/sumerios.png";
//            addWatermark(writer, rutaImagen, document);

            InputStream logoStream = PdfGenerator2.class.getResourceAsStream("/static/images/sumerios.png");
            if (logoStream != null) {
                addWatermark(writer, logoStream, document);
            } else {
                System.out.println("Marca de agua no encontrada. Continuando sin ella.");
            }

            // Configurar fuentes
            BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.EMBEDDED);
            Font titleFont = new Font(baseFont, 10, Font.BOLD);
            Font boldFont = new Font(baseFont, 8, Font.BOLD);
            Font regularFont = new Font(baseFont, 8, Font.NORMAL);
            Font smallFont = new Font(baseFont, 6, Font.NORMAL);

            // Color de fondo para las tablas destacadas (gris claro)
            BaseColor grayBackground = new BaseColor(230, 230, 230);

            // Título principal
            Paragraph title = new Paragraph("RECIBO DE EXPENSAS PROPIEDAD HORIZONTAL\n", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n", regularFont));

            // Tabla de ADMINISTRACION
            PdfPTable adminTable = new PdfPTable(1); // Cambiar a una sola columna
            adminTable.setWidthPercentage(100);
            adminTable.getDefaultCell().setBorder(PdfPCell.BOX); // Agregar bordes

            // Fila del título
            adminTable.addCell(createCell("ADMINISTRACION", titleFont, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground)).setPaddingBottom(4f);

            // Filas de datos
            PdfPTable adminDataTable = new PdfPTable(2); // Tabla interna para datos con dos columnas
            adminDataTable.setWidthPercentage(100);
            adminDataTable.setWidths(new int[]{25, 75});
            adminDataTable.getDefaultCell().setBorder(PdfPCell.BOX);
            adminDataTable.addCell(createCell("Nombre:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            adminDataTable.addCell(createCell(admDto.getNombre(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            adminDataTable.addCell(createCell("Administrador:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            adminDataTable.addCell(createCell(admDto.getAdministrador().getApellido() + ", " + admDto.getAdministrador().getNombre(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            adminDataTable.addCell(createCell("C.U.I.T:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            adminDataTable.addCell(createCell(admDto.getAdministrador().getCuit(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            adminTable.addCell(adminDataTable); // Agregar la tabla interna como celda

            document.add(adminTable);

            // Tabla de CONSORCIO
            PdfPTable consorcioTable = new PdfPTable(1); // Cambiar a una sola columna
            consorcioTable.setWidthPercentage(100);
            consorcioTable.getDefaultCell().setBorder(PdfPCell.BOX); // Agregar bordes

            // Fila del título
            consorcioTable.addCell(createCell("CONSORCIO", titleFont, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground)).setPaddingBottom(4f);


            // Filas de datos
            PdfPTable consorcioDataTable = new PdfPTable(2); // Tabla interna para datos con dos columnas
            consorcioDataTable.setWidthPercentage(100);
            consorcioDataTable.setWidths(new int[]{20, 80});
            consorcioDataTable.getDefaultCell().setBorder(PdfPCell.BOX);
            consorcioDataTable.addCell(createCell("Nombre:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell(consorcioDto.getNombre() + " - " + consorcioDto.getDireccion(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell("Propietario:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell(uf.getApellidoPropietario() + ", " + uf.getNombrePropietario(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell("UF:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell(uf.getUnidadFuncional() + " - " + uf.getTitulo(), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell("Período:", regularFont, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null));
            consorcioDataTable.addCell(createCell(pagoUF.getPeriodo().format(DateTimeFormatter.ofPattern("MM/yyyy")), regularFont, Element.ALIGN_RIGHT, PdfPCell.NO_BORDER, null));
            consorcioTable.addCell(consorcioDataTable); // Agregar la tabla interna como celda

            document.add(consorcioTable);

            // Detalles del pago (con fondo gris en "Pago ID")
            PdfPTable detallesTable = new PdfPTable(1);
            detallesTable.setWidthPercentage(100);
            detallesTable.getDefaultCell().setBorder(PdfPCell.BOX); // Agregar bordes

            detallesTable.addCell(createCell("Pago ID: " + pagoUF.getIdPagoUF(), boldFont, Element.ALIGN_JUSTIFIED, PdfPCell.BOX, grayBackground)).setPaddingBottom(4f);
            detallesTable.addCell(createAlignedCell("Total A:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getTotalA()), regularFont)).setPaddingTop(1f);
            detallesTable.addCell(createAlignedCell("Total B:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getTotalB()), regularFont));
            detallesTable.addCell(createAlignedCell("Total C:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getTotalC()), regularFont));
            detallesTable.addCell(createAlignedCell("Total D:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getTotalD()), regularFont));
            detallesTable.addCell(createAlignedCell("Total E:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getTotalE()), regularFont));
            detallesTable.addCell(createAlignedCell("Gasto Particular:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getGastoParticular()), regularFont));
            detallesTable.addCell(createAlignedCell("Pagado en el período:", "$" + formatCurrency(totalPago), regularFont)).setPaddingBottom(2f);
            detallesTable.addCell(createAlignedCell("Su pago:", "$" + formatCurrency(pagoUF.getValor()), boldFont)).setBorder(PdfPCell.TOP);
            detallesTable.addCell(createAlignedCell("Saldo:", "$" + formatCurrency(uf.getEstadoCuentaUfDTO().getSaldoFinal()), boldFont)).setBorder(PdfPCell.BOTTOM);

            document.add(detallesTable);


            // Información adicional
            Paragraph fechaPago = new Paragraph("Fecha de pago: " + pagoUF.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "\nForma de Pago: " + pagoUF.getFormaPago(), smallFont);
            fechaPago.setAlignment(Element.ALIGN_RIGHT);
            fechaPago.setSpacingAfter(5f);


            document.add(fechaPago);


            // Línea centrada con texto "EXPENSAS LEY 14,701 PROVINCIA DE BUENOS AIRES"
            PdfPTable leyTable = new PdfPTable(1);
            leyTable.setWidthPercentage(100);
            leyTable.addCell(createCell("EXPENSAS LEY 14,701 PROVINCIA DE BUENOS AIRES", boldFont, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground)).setPadding(4f);
            document.add(leyTable);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }


    // Método para crear una celda con fondo opcional
    private static PdfPCell createCell(String content, Font font, int alignment, int border, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    // Método para agregar la marca de agua centrada y con transparencia
    private static void addWatermark(PdfWriter writer, InputStream imageStream, Document document) throws Exception {
        PdfContentByte canvas = writer.getDirectContentUnder();
        Image watermark = Image.getInstance(IOUtils.toByteArray(imageStream));

        // Dimensiones de la marca de agua
        float watermarkWidth = 70;
        float watermarkHeight = 70;

        // Posición centrada
        float x = (document.getPageSize().getWidth() - watermarkWidth) / 2;
        float y = 308;

        watermark.setAbsolutePosition(x, y);
        watermark.scaleAbsolute(watermarkWidth, watermarkHeight);
        watermark.setTransparency(new int[]{0x10, 0x10});

        canvas.addImage(watermark);
    }

    private static PdfPCell createAlignedCell(String leftText, String rightText, Font font) {
        // Crear una tabla interna con dos columnas
        PdfPTable innerTable = new PdfPTable(2);
        innerTable.setWidthPercentage(100); // Ancho completo de la celda principal
        try {
            innerTable.setWidths(new int[]{70, 30}); // Ajusta las proporciones de las columnas
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        // Agregar el texto a la izquierda
        PdfPCell leftCell = new PdfPCell(new Phrase(leftText, font));
        leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        leftCell.setBorder(PdfPCell.NO_BORDER); // Sin borde
        innerTable.addCell(leftCell);

        // Agregar el texto a la derecha
        PdfPCell rightCell = new PdfPCell(new Phrase(rightText, font));
        rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        rightCell.setBorder(PdfPCell.NO_BORDER); // Sin borde
        innerTable.addCell(rightCell);

        // Crear la celda principal que contendrá la tabla interna
        PdfPCell cell = new PdfPCell(innerTable);
        cell.setBorder(PdfPCell.NO_BORDER); // Sin borde para la celda principal

        return cell;
    }

    private static String formatCurrency(double amount) {
        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.GERMANY); // Utiliza el formato europeo (punto como separador de miles y coma como separador decimal)
        numberFormat.setMinimumFractionDigits(2); // Para mostrar siempre 2 decimales
        numberFormat.setMaximumFractionDigits(2); // Máximo de 2 decimales
        return numberFormat.format(amount);
    }



}



package com.mad.sumerios.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.expensa.dto.ExpensaResponseDto;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfGeneratorExpensa {
    public static byte[] crearPdfExpensa(AdministracionResponseDTO adm,
                                         ConsorcioResponseDTO consorcio,
                                         ExpensaResponseDto expensa,
                                         List<UnidadFuncionalResponseDTO> ufs,
                                         List<EstadoCuentaUfDTO> estadosDeCuentaUf) throws DocumentException, IOException {

        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();

        // Agregar contenido al PDF
        Image logo = Image.getInstance("src/main/java/com/mad/sumerios/pdf/img/sumerios.png");
        logo.scaleToFit(100, 100);
        document.add(logo);

        document.add(new Paragraph("Expensa del Consorcio: " + consorcio.getNombre(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16)));
        document.add(new Paragraph("Administración: " + adm.getNombre()));
        document.add(new Paragraph("Periodo Liquidado: " + expensa.getPeriodo()));
        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Movimientos (Ingresos y Egresos):", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14)));

        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell("Fecha");
        tabla.addCell("Descripción");
        tabla.addCell("Tipo");
        tabla.addCell("Monto");

        document.add(tabla);
        document.close();

        return baos.toByteArray();
    }
}
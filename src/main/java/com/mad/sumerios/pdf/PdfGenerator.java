package com.mad.sumerios.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFCreateDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.mad.sumerios.unidadfuncional.model.UnidadFuncional;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PdfGenerator {

    public static void createPdf(PagoUFDTO pagoUF,double totalPago, AdministracionResponseDTO admDto, ConsorcioResponseDTO consorcioDto, UnidadFuncionalResponseDTO uf, String outputPath) {
        // Crear el documento con tamaño carta y márgenes
        Document document = new Document(PageSize.LETTER, 80, 80, 75, 75);

        try {
            // Asociar el PdfWriter con el archivo de salida
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            // Abrir el documento para agregar contenido
            document.open();
            Font boldFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.BLACK);
            // Agregar contenido al PDF
            document.add(new Paragraph("Detalles del Pago de la Unidad Funcional\n\n", boldFont));
            document.add(new Paragraph("Pago ID: " + pagoUF.getIdPagoUF()+"\n"));
            document.add(new Paragraph("ADMINISTRACION", boldFont));
            document.add(new Paragraph(admDto.getNombre(),boldFont));
            document.add(new Paragraph("Administrador: " + admDto.getAdministrador().getApellido()+ ", "+ admDto.getAdministrador().getNombre()+".\n"));
            document.add(new Paragraph("--------------------------------------------\n"));
            document.add(new Paragraph("CONSORCIO", boldFont));
            document.add(new Paragraph(consorcioDto.getNombre() + " - " +consorcioDto.getDireccion()));
            document.add(new Paragraph("Propietario: " + uf.getApellidoPropietario()+", "+ uf.getNombrePropietario()+"\n"));
            document.add(new Paragraph("UF: " + uf.getUnidadFuncional() + " - " +uf.getTitulo()));
            document.add(new Paragraph("Periodo: " + pagoUF.getPeriodo().format(DateTimeFormatter.ofPattern("MM/yyyy"))));
            document.add(new Paragraph("--------------------------------------------"));
            document.add(new Paragraph("Total A: " + uf.getEstadoCuentaUfDTO().getTotalA()));
            document.add(new Paragraph("Total B: " + uf.getEstadoCuentaUfDTO().getTotalB()));
            document.add(new Paragraph("Total C: " + uf.getEstadoCuentaUfDTO().getTotalC()));
            document.add(new Paragraph("Total D: " + uf.getEstadoCuentaUfDTO().getTotalD()));
            document.add(new Paragraph("Total E: " + uf.getEstadoCuentaUfDTO().getTotalE()));
            document.add(new Paragraph("Total Gasto Particular: " + uf.getEstadoCuentaUfDTO().getGastoParticular()+"\n"));
            document.add(new Paragraph("Total pagado hasta el momento: " + totalPago, boldFont));

            document.add(new Paragraph("Pago: $" + pagoUF.getValor()));
            document.add(new Paragraph("Saldo: " + uf.getEstadoCuentaUfDTO().getTotalFinal(),boldFont));
            document.add(new Paragraph("Fecha:\n" + pagoUF.getFecha()));
            document.add(new Paragraph("Forma de Pago:\n" + pagoUF.getFormaPago()));


            // Manejar caso de descripción nula
            if (pagoUF.getDescripcion() != null) {
                document.add(new Paragraph("Descripción:\n", boldFont));
                document.add(new Paragraph(pagoUF.getDescripcion()));

            }

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}

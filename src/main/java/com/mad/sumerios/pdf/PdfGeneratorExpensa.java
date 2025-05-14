package com.mad.sumerios.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.mad.sumerios.administracion.dto.AdministracionResponseDTO;
import com.mad.sumerios.consorcio.dto.ConsorcioResponseDTO;
import com.mad.sumerios.enums.CategoriaEgreso;
import com.mad.sumerios.enums.TipoEgreso;
import com.mad.sumerios.estadocuentauf.dto.EstadoCuentaUfDTO;
import com.mad.sumerios.expensa.dto.ExpensaResponseDto;
import com.mad.sumerios.movimientos.egreso.dto.EgresoResponseDTO;
import com.mad.sumerios.movimientos.gastoParticular.dto.GastoParticularResponseDTO;
import com.mad.sumerios.movimientos.pagouf.dto.PagoUFDTO;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PdfGeneratorExpensa {
    public static byte[] crearPdfExpensa(AdministracionResponseDTO adm,
                                         ConsorcioResponseDTO consorcio,
                                         ExpensaResponseDto expensa,
                                         List<UnidadFuncionalResponseDTO> ufs,
                                         List<EstadoCuentaUfDTO> estadosDeCuentaUf,
                                         Boolean segundoVencimiento,
                                         String nota,
                                         String juicios) throws DocumentException, IOException {

        float margenSuperior = 280f; // ajusta este valor según el alto del encabezado
        Document document = new Document(PageSize.B4.rotate(), 36, 36, margenSuperior, 36);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);

        // =====================
        // FORMATEOS Y DATOS PREVIOS
        // =====================
        String mes = expensa.getPeriodo().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String mesCapitalizado = mes.substring(0, 1).toUpperCase() + mes.substring(1);
        String periodoFormateado = mesCapitalizado + " de " + expensa.getPeriodo().getYear();

        // =====================
        // DINERO
        // =====================
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));


        // =====================
        // FUENTES Y COLORES
        // =====================
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.WHITE);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
        Font boldFontBigTitle = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font boldFontTitle = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font boldFontSubTitle = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
        Font font = new Font(Font.FontFamily.HELVETICA, 14);
        Font fonter = new Font(Font.FontFamily.HELVETICA, 12);
        Font regularFont = new Font(Font.FontFamily.HELVETICA, 10);
        Font boldFontSmall = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        Font boldFontSmaller = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 8);
        Font smallFonter = new Font(Font.FontFamily.HELVETICA, 7);

        BaseColor grayBackground = new BaseColor(230, 230, 230);
        BaseColor verdeAzulado = new BaseColor(0, 128, 128);


        try {
            // AGREGO TITULO, ENCABEZADO Y LOGO
            EncabezadoExpensas headerEvent = new EncabezadoExpensas(250, 250, 300,
                    titleFont,
                    subtitleFont,
                    boldFontBigTitle,
                    boldFontTitle,
                    verdeAzulado,
                    grayBackground,
                    periodoFormateado,
                    adm,
                    consorcio
            );
            writer.setPageEvent(headerEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.open();



//      EGRESOS, GASTOS PARTICULARES E INGRESOS
//      TITULO
        PdfPTable tituloMovimientos = new PdfPTable(1); // Cambiar a una sola columna
        tituloMovimientos.setWidthPercentage(100);
        tituloMovimientos.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell movimientosCell = new PdfPCell(new Phrase("Pagos e ingresos del período", titleFont));
        movimientosCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        movimientosCell.setBackgroundColor(verdeAzulado);
        movimientosCell.setBorder(PdfPCell.BOX);
        movimientosCell.setPaddingBottom(15f);
        movimientosCell.setPaddingTop(15f);

        tituloMovimientos.addCell(movimientosCell);

        document.add(tituloMovimientos);

//      ENCABEZADO DE TABLA
        PdfPTable encabezadoTablaMovimientos = new PdfPTable(7); // Cambiar a una sola columna
        encabezadoTablaMovimientos.setWidthPercentage(100);
        encabezadoTablaMovimientos.setWidths(new int[]{40,10,10,10,10,10,10});

        encabezadoTablaMovimientos.addCell(createCell("Descripción del gastos ", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("A", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("B", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("C", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("D", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("E", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("Total", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,25f)).setPaddingBottom(4f);

        encabezadoTablaMovimientos.setSpacingAfter(20f);

        document.add(encabezadoTablaMovimientos);

        int numeroTipoEgreso = 1;
        for (TipoEgreso tipo : TipoEgreso.values()) {
            // Encabezado de la tabla
            PdfPTable table = new PdfPTable(7); //
            table.setWidthPercentage(100);
            table.setWidths(new int[]{40,10,10,10,10,10,10});

            PdfPCell cell = new PdfPCell(createCell(numeroTipoEgreso + " - " + tipo.getDescripcion().toUpperCase(),boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground,23f));

            cell.setColspan(8);
            cell.setPadding(5f);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);


            // Filas de datos
            List<EgresoResponseDTO> egresos = filtrarPorTipoEgreso(expensa.getEgresos(), tipo);
            if(!egresos.isEmpty()){
                double totalRubro = 0;
                int numEgreso = 1;

                for (EgresoResponseDTO egreso : egresos) {
                    Paragraph paragraph = new Paragraph();
                    paragraph.add(new Chunk(numEgreso+" - "+egreso.getTitulo() + ":", boldFontTitle).setTextRise(4f));

                    Chunk descripcionChunk = new Chunk(egreso.getDescripcion(), regularFont);
                    descripcionChunk.setTextRise(-2f);
                    paragraph.add(Chunk.NEWLINE);
                    paragraph.add(descripcionChunk);

                    table.addCell(createCellEgreso(paragraph, Element.ALIGN_LEFT, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.A) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.B) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.C) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.D) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.E) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null,20f));
                    table.addCell(createCell(formatoMoneda.format(egreso.getTotalFinal()), boldFontSubTitle, Element.ALIGN_RIGHT, PdfPCell.BOX, null,20f));
                    totalRubro += egreso.getTotalFinal();

                    numEgreso++;

                }
                PdfPCell pie = new PdfPCell(createCell("Total del rubro",boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground,23f));
                pie.setPadding(5f);
                pie.setColspan(6);
                table.addCell(pie);
                table.addCell(createCell(formatoMoneda.format(totalRubro),boldFontSubTitle, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,23f)).setPadding(5f);

                table.setSpacingAfter(10f);
                numeroTipoEgreso ++;

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            // Pie de la tabla

        }

//      FIN EGRESOS, GASTOS PARTICULARES E INGRESOS

//      ESTADO CUENTA CONSORCIO
        document.newPage();
        PdfPTable estadoCuentaConsorcio = new PdfPTable(1); // Cambiar a una sola columna
        estadoCuentaConsorcio.setWidthPercentage(100);
        estadoCuentaConsorcio.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell ecTituloEstadoCuentaConsorcioCell = new PdfPCell(new Phrase("Estado financiero", titleFont));
        ecTituloEstadoCuentaConsorcioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ecTituloEstadoCuentaConsorcioCell.setBackgroundColor(verdeAzulado);
        ecTituloEstadoCuentaConsorcioCell.setBorder(PdfPCell.BOX);
        ecTituloEstadoCuentaConsorcioCell.setPaddingBottom(15f);
        ecTituloEstadoCuentaConsorcioCell.setPaddingTop(15f);

        estadoCuentaConsorcio.addCell(ecTituloEstadoCuentaConsorcioCell);

        document.add(estadoCuentaConsorcio);
//      FIN ESTADO CUENTA CONSORCIO

//      NOTA Y JUICIOS

        document.newPage();
        PdfPTable notaTabla = new PdfPTable(1); // Cambiar a una sola columna
        notaTabla.setWidthPercentage(100);
        notaTabla.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell notaTituloCell = new PdfPCell(new Phrase("NOTA", titleFont));
        notaTituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        notaTituloCell.setBackgroundColor(verdeAzulado);
        notaTituloCell.setBorder(PdfPCell.BOX);
        notaTituloCell.setPaddingBottom(15f);
        notaTituloCell.setPaddingTop(15f);
        notaTabla.addCell(notaTituloCell);

        PdfPCell notaContenidoCell = new PdfPCell(new Phrase(
                (nota != null && !nota.trim().isEmpty()) ? nota : "No hay notas este mes", font));
        notaContenidoCell.setBackgroundColor(grayBackground);
        notaContenidoCell.setBorder(PdfPCell.BOX);
        notaContenidoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        notaContenidoCell.setPadding(15f);
        notaTabla.addCell(notaContenidoCell);

        PdfPCell cierreNota = new PdfPCell();
        cierreNota.setBackgroundColor(verdeAzulado);
        cierreNota.setBorder(PdfPCell.BOX);
        cierreNota.setMinimumHeight(25f);

        notaTabla.addCell(cierreNota);
        notaTabla.setSpacingAfter(20f);

        document.add(notaTabla);

        PdfPTable juiciosTabla = new PdfPTable(1); // Cambiar a una sola columna
        juiciosTabla.setWidthPercentage(100);
        juiciosTabla.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell juiciosTituloCell = new PdfPCell(new Phrase("DATOS DE JUICIOS", titleFont));
        juiciosTituloCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        juiciosTituloCell.setBackgroundColor(verdeAzulado);
        juiciosTituloCell.setBorder(PdfPCell.BOX);
        juiciosTituloCell.setPaddingBottom(15f);
        juiciosTituloCell.setPaddingTop(15f);
        juiciosTabla.addCell(juiciosTituloCell);

        PdfPCell juiciosContenidoCell = new PdfPCell(new Phrase(
                (juicios != null && !juicios.trim().isEmpty()) ? juicios : "No hay juicios iniciados", font));
        juiciosContenidoCell.setBackgroundColor(grayBackground);
        juiciosContenidoCell.setBorder(PdfPCell.BOX);
        juiciosContenidoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        juiciosContenidoCell.setPadding(15f);
        juiciosTabla.addCell(juiciosContenidoCell);

        PdfPCell cierreJuicios = new PdfPCell();
        cierreJuicios.setBackgroundColor(verdeAzulado);
        cierreJuicios.setBorder(PdfPCell.BOX);
        cierreJuicios.setMinimumHeight(25f);
        juiciosTabla.addCell(cierreJuicios);

        juiciosTabla.setSpacingAfter(20f);

        document.add(juiciosTabla);

//      FIN NOTA Y JUICIOS

//      ESTADO CUENTA UNIDADES FUNCIONALES
        document.newPage();

        PdfPTable estadoCuentaUf = new PdfPTable(1);
        estadoCuentaUf.setWidthPercentage(100);
        estadoCuentaUf.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell ecTituloEstadoCuentaUfCell = new PdfPCell(new Phrase("Estado de cuenta y prorrateo de gastos", titleFont));
        ecTituloEstadoCuentaUfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ecTituloEstadoCuentaUfCell.setBackgroundColor(verdeAzulado);
        ecTituloEstadoCuentaUfCell.setBorder(PdfPCell.BOX);
        ecTituloEstadoCuentaUfCell.setPaddingBottom(15f);
        ecTituloEstadoCuentaUfCell.setPaddingTop(15f);

        estadoCuentaUf.addCell(ecTituloEstadoCuentaUfCell);

        document.add(estadoCuentaUf);


        if(segundoVencimiento){
            PdfPTable encabezadoTablaEstadoCuentaUf = new PdfPTable(20); // Número total de columnas
            encabezadoTablaEstadoCuentaUf.setWidthPercentage(100);

            encabezadoTablaEstadoCuentaUf.setWidths(new int[]{2, 3, 6,
                    6, 6, 6, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    6,
                    7,
                    7});

            // Fila 1: Encabezados principales con celdas combinadas
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Datos de la unidad", boldFontTitle, Element.ALIGN_CENTER, 3));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Cuenta corriente", boldFontTitle, Element.ALIGN_CENTER, 4));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("A", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("B", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("C", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("D", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("E", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Gastos part.", boldFontTitle, Element.ALIGN_CENTER, 1));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Expensa\n 1° Vto.", boldFontTitle, Element.ALIGN_CENTER, 1));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Expensa\n 2° Vto.", boldFontTitle, Element.ALIGN_CENTER, 1));

            // Fila 2: Subtítulos
            encabezadoTablaEstadoCuentaUf.addCell(createCell("UF", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("N°", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Propietario", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Saldo $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Pago $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Deuda $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Int. $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f)); // Gastos part.
            encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f)); // Total final 1Vto
            encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f)); // Total final 2Vto


            document.add(encabezadoTablaEstadoCuentaUf);


            PdfPTable tablaEstadoCuenta = new PdfPTable(20); //
            tablaEstadoCuenta.setWidthPercentage(100);
            tablaEstadoCuenta.setWidths(new int[]{2, 3, 6,
                    6, 6, 6, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    6,
                    7,
                    7});

            double saldo = 0,
                    pago = 0,
                    deuda = 0,
                    intereses = 0,
                    porA = 0,
                    totalA = 0,
                    porB = 0,
                    totalB = 0,
                    porC = 0,
                    totalC = 0,
                    porD = 0,
                    totalD = 0,
                    porE = 0,
                    totalE = 0,
                    totalGp = 0,
                    total = 0,
                    totalSegundoVencimiento = 0;

            for (UnidadFuncionalResponseDTO uf : ufs) {
                // Encabezado de la tabla
                EstadoCuentaUfDTO estadoCuenta = estadosDeCuentaUf.stream()
                        .filter(e -> e.getIdUf().equals(uf.getIdUf()))
                        .findFirst()
                        .orElse(null);

                List<PagoUFDTO> pagos = expensa.getPagoUf().stream().filter(e -> e.getIdUf().equals(uf.getIdUf())).toList();
                List<GastoParticularResponseDTO> gP = expensa.getGp().stream().filter(e -> e.getIdUf().equals(uf.getIdUf())).toList();
                double totalPago = totalPagoPorUf(pagos);
                double totalGastosParticulares = totalGastoParticulares(gP);

                saldo += estadoCuenta.getTotalMesPrevio();
                pago += totalPago;
                deuda += estadoCuenta.getDeuda();
                intereses += estadoCuenta.getIntereses();
                porA += uf.getPorcentajeUnidad();
                totalA += estadoCuenta.getTotalA();
                porB += uf.getPorcentajeUnidadB();
                totalB += estadoCuenta.getTotalB();
                porC += uf.getPorcentajeUnidadC();
                totalC += estadoCuenta.getTotalC();
                porD += uf.getPorcentajeUnidadD();
                totalD += estadoCuenta.getTotalD();
                porE += uf.getPorcentajeUnidadE();
                totalE += estadoCuenta.getTotalE();
                totalGp += totalGastosParticulares;
                total += estadoCuenta.getTotalExpensa();
                totalSegundoVencimiento += estadoCuenta.getSegundoVencimiento();

                // DATOS UF
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getUnidadFuncional()), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(uf.getTitulo(), boldFontSmall, Element.ALIGN_LEFT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(uf.getApellidoPropietario()+", "+uf.getNombrePropietario(), boldFontSmall, Element.ALIGN_LEFT, PdfPCell.BOX, null, 20f));
                // SALDOS UF
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalMesPrevio()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalPago), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getDeuda()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getIntereses()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                // TOTALES
                // A
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidad()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalA()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // B
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadB()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalB()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // C
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadC()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalC()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // D
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadD()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalD()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // E
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadE()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalE()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // GASTO PART
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalGastosParticulares), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // TOTAL A PAGAR
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalExpensa()), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // TOTAL A PAGAR
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getSegundoVencimiento()), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));


            }
            document.add(tablaEstadoCuenta);

            PdfPTable tablaTotales = new PdfPTable(20); //
            tablaTotales.setWidthPercentage(100);
            tablaTotales.setWidths(new int[]{2, 3, 6,
                    6, 6, 6, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    3, 6,
                    6,
                    7,
                    7});

            // Fila 1: Encabezados principales con celdas combinadas
            tablaTotales.addCell(createCellTotales("TOTALES", boldFontSubTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground, 3,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(saldo), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(pago), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(deuda), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(intereses), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porA, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalA), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porB, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalB), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porC, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalC), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porD, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalD), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porE, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalE), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalGp), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(total), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalSegundoVencimiento), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));


            document.add(tablaTotales);
        } else {
            PdfPTable encabezadoTablaEstadoCuentaUf = new PdfPTable(19); // Número total de columnas
            encabezadoTablaEstadoCuentaUf.setWidthPercentage(100);

            encabezadoTablaEstadoCuentaUf.setWidths(new int[]{2, 3, 8,
                    6, 6, 6, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    6,
                    7});

            // Fila 1: Encabezados principales con celdas combinadas
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Datos de la unidad", boldFontTitle, Element.ALIGN_CENTER, 3));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Cuenta corriente", boldFontTitle, Element.ALIGN_CENTER, 4));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("A", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("B", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("C", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("D", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("E", boldFontTitle, Element.ALIGN_CENTER, 2));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Gastos part.", boldFontTitle, Element.ALIGN_CENTER, 1));
            encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Total a abonar", boldFontTitle, Element.ALIGN_CENTER, 1));

            // Fila 2: Subtítulos
            encabezadoTablaEstadoCuentaUf.addCell(createCell("UF", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("N°", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Propietario", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Saldo $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Pago $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Deuda $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Int. $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f)); // Gastos part.
            encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f)); // Total final

            document.add(encabezadoTablaEstadoCuentaUf);


            PdfPTable tablaEstadoCuenta = new PdfPTable(19); //
            tablaEstadoCuenta.setWidthPercentage(100);
            tablaEstadoCuenta.setWidths(new int[]{2, 3, 8,
                    6, 6, 6, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    6,
                    7});

            double saldo = 0, pago = 0, deuda = 0, intereses = 0, porA = 0, totalA = 0, porB = 0, totalB = 0, porC = 0, totalC = 0, porD = 0, totalD = 0, porE = 0, totalE = 0, totalGp = 0, total = 0;

            for (UnidadFuncionalResponseDTO uf : ufs) {
                // Encabezado de la tabla
                EstadoCuentaUfDTO estadoCuenta = estadosDeCuentaUf.stream()
                        .filter(e -> e.getIdUf().equals(uf.getIdUf()))
                        .findFirst()
                        .orElse(null);

                List<PagoUFDTO> pagos = expensa.getPagoUf().stream().filter(e -> e.getIdUf().equals(uf.getIdUf())).toList();
                List<GastoParticularResponseDTO> gP = expensa.getGp().stream().filter(e -> e.getIdUf().equals(uf.getIdUf())).toList();
                double totalPago = totalPagoPorUf(pagos);
                double totalGastosParticulares = totalGastoParticulares(gP);

                saldo += estadoCuenta.getTotalMesPrevio();
                pago += totalPago;
                deuda += estadoCuenta.getDeuda();
                intereses += estadoCuenta.getIntereses();
                porA += uf.getPorcentajeUnidad();
                totalA += estadoCuenta.getTotalA();
                porB += uf.getPorcentajeUnidadB();
                totalB += estadoCuenta.getTotalB();
                porC += uf.getPorcentajeUnidadC();
                totalC += estadoCuenta.getTotalC();
                porD += uf.getPorcentajeUnidadD();
                totalD += estadoCuenta.getTotalD();
                porE += uf.getPorcentajeUnidadE();
                totalE += estadoCuenta.getTotalE();
                totalGp += totalGastosParticulares;
                total += estadoCuenta.getTotalExpensa();

                if (estadoCuenta == null) continue;
                // DATOS UF
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getUnidadFuncional()), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(uf.getTitulo(), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(uf.getApellidoPropietario()+", "+uf.getNombrePropietario(), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                // SALDOS UF
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalMesPrevio()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalPago), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getDeuda()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getIntereses()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));
                // TOTALES
                // A
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidad()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalA()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // B
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadB()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalB()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // C
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadC()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalC()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // D
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadD()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalD()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // E
                tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadE()), smallFont, Element.ALIGN_CENTER, PdfPCell.BOX, null, 20f));
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalE()), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // GASTO PART
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalGastosParticulares), smallFont, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));

                // TOTAL A PAGAR
                tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalExpensa()), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, null, 20f));


            }
            document.add(tablaEstadoCuenta);

            PdfPTable tablaTotales = new PdfPTable(19); //
            tablaTotales.setWidthPercentage(100);
            tablaTotales.setWidths(new int[]{2, 3, 8,
                    6, 6, 6, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    4, 6,
                    6,
                    7});

            // Fila 1: Encabezados principales con celdas combinadas
            tablaTotales.addCell(createCellTotales("TOTALES", boldFontSubTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground, 3,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(saldo), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(pago), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(deuda), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(intereses), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porA, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalA), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porB, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalB), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porC, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalC), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porD, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalD), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(String.valueOf(redondear(porE, 2)), boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalE), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(totalGp), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));
            tablaTotales.addCell(createCell(formatoMoneda.format(total), boldFontSmall, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground,20f));

            document.add(tablaTotales);
        }
//      FIN ESTADO CUENTA UNIDADES FUNCIONALES

//        document.setPageSize(PageSize.A4);
        document.newPage(); // <-- Salto de página
        document.close();

        return baos.toByteArray();
    }

    private static PdfPCell createCell(String content, Font font, int alignment, int border, BaseColor backgroundColor, Float minHeight) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment); // izquierda, centro, derecha
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // centrado vertical
        cell.setBorder(border);

        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minHeight != null) {
            cell.setMinimumHeight(minHeight);
        }
        return cell;
    }

    private static PdfPCell createCellEc(String content, Font font, int alignment, int border, BaseColor backgroundColor, Float minHeight) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // centrado vertical
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minHeight != null) {
            cell.setMinimumHeight(minHeight);
        }
        cell.setPadding(2);

        return cell;
    }

    private static PdfPCell createCellTotales(String content, Font font, int alignment, int border, BaseColor backgroundColor, int colspan,Float minHeight) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // centrado vertical
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minHeight != null) {
            cell.setMinimumHeight(minHeight);
        }
        return cell;
    }

    private static PdfPCell createMergedCell(String text, Font font, int alignment, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setColspan(colspan); // Fusiona las celdas en el número especificado
        cell.setHorizontalAlignment(alignment); // Alineación del texto
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE); // Alineación vertical centrada
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY); // Fondo gris para diferenciación
        cell.setPadding(5); // Espaciado interno para mejorar la apariencia
        return cell;
    }

    private static PdfPCell createCellEgreso(Paragraph content, int hAlign, int border, BaseColor backgroundColor, Float minHeight) {
        PdfPCell cell = new PdfPCell(content);
        cell.setHorizontalAlignment(hAlign);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBorder(border);
        cell.setPadding(8f); // agrega espaciado interno
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        if (minHeight != null) {
            cell.setMinimumHeight(minHeight);
        }
        return cell;
    }

    public static List<EgresoResponseDTO> filtrarPorTipoEgreso(List<EgresoResponseDTO> dtos, TipoEgreso tipo) {
        return dtos.stream()
                .filter(egreso -> egreso.getTipoEgreso().equals(tipo))
                .collect(Collectors.toList());
    }

    public static double totalPagoPorUf(List<PagoUFDTO> dtos){
        double suma = 0.0;
        for(PagoUFDTO pagoUFDTO : dtos){
            suma += pagoUFDTO.getValor();
        }
        return suma;
    }

    public static double totalGastoParticulares(List<GastoParticularResponseDTO> dtos){
        double suma = 0.0;
        for(GastoParticularResponseDTO gP : dtos){
            suma += gP.getTotalFinal();
        }
        return suma;
    }

    public static double redondear(double valor, int decimales) {
        double escala = Math.pow(10, decimales);
        return Math.round(valor * escala) / escala;
    }


}
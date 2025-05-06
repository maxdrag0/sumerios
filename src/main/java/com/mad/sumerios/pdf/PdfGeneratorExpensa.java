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
import org.apache.pdfbox.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
                                         List<EstadoCuentaUfDTO> estadosDeCuentaUf) throws DocumentException, IOException {

        Document document = new Document(PageSize.B4.rotate());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, baos);
        try {
            LogoHeaderEvent logoEvent = new LogoHeaderEvent(250, 250, 300); // Ajustá ancho, alto y offsetY
            writer.setPageEvent(logoEvent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        document.open();
        // FORMATOS, FUENTES Y COLORES
        // PERIODO
        String mes = expensa.getPeriodo().getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        String mesCapitalizado = mes.substring(0, 1).toUpperCase() + mes.substring(1);
        String periodoFormateado = mesCapitalizado + " de " + expensa.getPeriodo().getYear();

        // DINERO
        NumberFormat formatoMoneda = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        // FUENTES
        // TITULOS FONDO VERDE
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, BaseColor.WHITE);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

        Font boldFontBigTitle = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font boldFontTitle = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
        Font boldFontSubTitle = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);


        Font font = new Font(Font.FontFamily.HELVETICA, 14);
        Font regularFont = new Font(Font.FontFamily.HELVETICA, 10);

        Font boldFontSmall = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
        Font boldFontSmaller = new Font(Font.FontFamily.HELVETICA, 7, Font.BOLD);
        Font smallFont = new Font(Font.FontFamily.HELVETICA, 8);
        Font smallFonter = new Font(Font.FontFamily.HELVETICA, 7);
        // COLORES
        BaseColor grayBackground = new BaseColor(230, 230, 230);
        BaseColor verdeAzulado = new BaseColor(0,128,128);

//        agregarLogo(document, 300, 300, 350); // ancho, alto, offset desde arriba

        // TABLA TITULO
        PdfPTable titulo = new PdfPTable(1); // Cambiar a una sola columna
        titulo.setWidthPercentage(100);
        titulo.getDefaultCell().setBorder(PdfPCell.BOX);
        titulo.setSpacingBefore(5f);

        PdfPCell titleCell = new PdfPCell(new Phrase("EXPENSAS LEY 14.701", subtitleFont));
        titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        titleCell.setBackgroundColor(verdeAzulado);
        titleCell.setBorder(PdfPCell.NO_BORDER);
        titleCell.setPaddingTop(15f);

        titulo.addCell(titleCell);



        PdfPCell subtitleCell = new PdfPCell(new Phrase("Liquidación de: "+periodoFormateado+"\n"+" ", titleFont));
        subtitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        subtitleCell.setBackgroundColor(verdeAzulado);
        subtitleCell.setBorder(PdfPCell.NO_BORDER);

        titulo.addCell(subtitleCell);

        document.add(titulo);
        // FIN TABLA TITULO

        // Tabla de DATOS ENCABEZADO
        PdfPTable encabezado = new PdfPTable(2); // Cambiar a una sola columna
        encabezado.setWidthPercentage(100);
        encabezado.setWidths(new int[]{50, 50});

        encabezado.addCell(createCell("ADMINISTRACION", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground)).setPaddingBottom(4f);
        encabezado.addCell(createCell("CONSORCIO", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground)).setPaddingBottom(4f);
        encabezado.addCell(createCell("Nombre: "+adm.getNombre(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("Nombre: "+consorcio.getNombre(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("Administrador: "+adm.getAdministrador().getNombre()+" "+adm.getAdministrador().getApellido(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);
        encabezado.addCell(createCell("Direccion: "+consorcio.getDireccion(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);
        encabezado.addCell(createCell("Direccion: "+adm.getDireccion(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("C.U.I.T.: "+consorcio.getCuit(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("Telefono: "+adm.getTelefono(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("Mail: "+adm.getMail(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("C.U.I.T.: "+adm.getCuit(), boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;
        encabezado.addCell(createCell("", boldFontTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, null)).setPaddingBottom(4f);;

        encabezado.setSpacingAfter(10f);
        document.add(encabezado);

        // FIN Tabla de DATOS ENCABEZADO

//      EGRESOS, GASTOS PARTICULARES E INGRESOS
//      TITULO
        PdfPTable tituloMovimientos = new PdfPTable(1); // Cambiar a una sola columna
        tituloMovimientos.setWidthPercentage(100);
        tituloMovimientos.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell movimientosCell = new PdfPCell(new Phrase("Pagos e ingresos del período", titleFont));
        movimientosCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        movimientosCell.setBackgroundColor(verdeAzulado);
        movimientosCell.setBorder(PdfPCell.NO_BORDER);
        movimientosCell.setPaddingBottom(15f);
        movimientosCell.setPaddingTop(15f);

        tituloMovimientos.addCell(movimientosCell);

        document.add(tituloMovimientos);

//      ENCABEZADO DE TABLA
        PdfPTable encabezadoTablaMovimientos = new PdfPTable(7); // Cambiar a una sola columna
        encabezadoTablaMovimientos.setWidthPercentage(100);
        encabezadoTablaMovimientos.setWidths(new int[]{40,10,10,10,10,10,10});

        encabezadoTablaMovimientos.addCell(createCell("Descripción del gastos ", boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.NO_BORDER, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("A", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("B", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("C", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("D", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("E", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);
        encabezadoTablaMovimientos.addCell(createCell("Total", boldFontBigTitle, Element.ALIGN_CENTER, PdfPCell.LEFT, grayBackground)).setPaddingBottom(4f);

        encabezadoTablaMovimientos.setSpacingAfter(10f);

        document.add(encabezadoTablaMovimientos);

        int numeroTipoEgreso = 1;
        for (TipoEgreso tipo : TipoEgreso.values()) {
            // Encabezado de la tabla
            PdfPTable table = new PdfPTable(7); //
            table.setWidthPercentage(100);
            table.setWidths(new int[]{40,10,10,10,10,10,10});

//            PdfPCell cell = new PdfPCell(new Phrase((numeroTipoEgreso + " - " + tipo.name().toUpperCase())));
            PdfPCell cell = new PdfPCell(createCell(numeroTipoEgreso + " - " + tipo.getDescripcion().toUpperCase(),boldFontBigTitle, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground));

            cell.setColspan(8);
            cell.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.addCell(cell);

            // Filas de datos
            List<EgresoResponseDTO> egresos = filtrarPorTipoEgreso(expensa.getEgresos(), tipo);
            if(!egresos.isEmpty()){
                double totalRubro = 0;
                for (EgresoResponseDTO egreso : egresos) {

                    table.addCell(createCell(egreso.getTitulo()+": "+egreso.getDescripcion(), font, Element.ALIGN_LEFT, PdfPCell.BOX, null));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.A) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.B) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.C) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.D) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null));
                    table.addCell(createCell(egreso.getCategoriaEgreso().equals(CategoriaEgreso.E) ? formatoMoneda.format(egreso.getTotalFinal()) : formatoMoneda.format(0), regularFont, Element.ALIGN_CENTER, PdfPCell.BOX, null));
                    table.addCell(createCell(formatoMoneda.format(egreso.getTotalFinal()), boldFontSubTitle, Element.ALIGN_RIGHT, PdfPCell.BOX, null));
                    totalRubro += egreso.getTotalFinal();

                    numeroTipoEgreso += 1;
                }
                PdfPCell pie = new PdfPCell(createCell("Total del rubro",boldFontTitle, Element.ALIGN_LEFT, PdfPCell.BOX, grayBackground));
                pie.setColspan(6);
                table.addCell(pie);
                table.addCell(createCell(formatoMoneda.format(totalRubro),boldFontTitle, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));

                table.setSpacingAfter(15f);

                document.add(table);
                document.add(Chunk.NEWLINE);
            }

            // Pie de la tabla

        }

//      FIN EGRESOS, GASTOS PARTICULARES E INGRESOS

//      ESTADO CUENTA CONSORCIO
        document.newPage();
        document.add(titulo);
        document.add(encabezado);
//        agregarLogo(document, 300, 300, 350); // ancho, alto, offset desde arriba


        PdfPTable estadoCuentaConsorcio = new PdfPTable(1); // Cambiar a una sola columna
        estadoCuentaConsorcio.setWidthPercentage(100);
        estadoCuentaConsorcio.getDefaultCell().setBorder(PdfPCell.BOX);

        PdfPCell ecTituloEstadoCuentaConsorcioCell = new PdfPCell(new Phrase("Estado financiero", titleFont));
        ecTituloEstadoCuentaConsorcioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        ecTituloEstadoCuentaConsorcioCell.setBackgroundColor(verdeAzulado);
        ecTituloEstadoCuentaConsorcioCell.setBorder(PdfPCell.NO_BORDER);
        ecTituloEstadoCuentaConsorcioCell.setPaddingBottom(15f);
        ecTituloEstadoCuentaConsorcioCell.setPaddingTop(15f);

        estadoCuentaConsorcio.addCell(ecTituloEstadoCuentaConsorcioCell);

        document.add(estadoCuentaConsorcio);
         // <-- Salto de página
//      FIN ESTADO CUENTA CONSORCIO

//      ESTADO CUENTA UNIDADES FUNCIONALES
        document.newPage();
        document.add(titulo);
        document.add(encabezado);

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


        PdfPTable encabezadoTablaEstadoCuentaUf = new PdfPTable(19); // Número total de columnas
        encabezadoTablaEstadoCuentaUf.setWidthPercentage(100);

        encabezadoTablaEstadoCuentaUf.setWidths(new int[]{2,3,8 ,
                6,6,6,6,
                4,6,
                4,6,
                4,6,
                4,6,
                4,6,
                6,
                7});

        // Fila 1: Encabezados principales con celdas combinadas
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Datos de la unidad", boldFontSubTitle, Element.ALIGN_CENTER, 3));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Cuenta corriente", boldFontSubTitle, Element.ALIGN_CENTER, 4));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("A", boldFontSubTitle, Element.ALIGN_CENTER, 2));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("B", boldFontSubTitle, Element.ALIGN_CENTER, 2));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("C", boldFontSubTitle, Element.ALIGN_CENTER, 2));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("D", boldFontSubTitle, Element.ALIGN_CENTER, 2));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("E", boldFontSubTitle, Element.ALIGN_CENTER, 2));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Gastos part.", boldFontSubTitle, Element.ALIGN_CENTER, 1));
        encabezadoTablaEstadoCuentaUf.addCell(createMergedCell("Total a abonar", boldFontSubTitle, Element.ALIGN_CENTER, 1));

        // Fila 2: Subtítulos
        encabezadoTablaEstadoCuentaUf.addCell(createCell("UF", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Propietario", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Saldo $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Pago $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Deuda $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Int. $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("%", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("Exp $", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground)); // Gastos part.
        encabezadoTablaEstadoCuentaUf.addCell(createCell("$", boldFontSmall, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground)); // Total final

        document.add(encabezadoTablaEstadoCuentaUf);


        PdfPTable tablaEstadoCuenta = new PdfPTable(19); //
        tablaEstadoCuenta.setWidthPercentage(100);
        tablaEstadoCuenta.setWidths(new int[]{2,3,8 ,
                6,6,6,6,
                4,6,
                4,6,
                4,6,
                4,6,
                4,6,
                6,
                7});

        double saldo = 0, pago= 0, deuda= 0, intereses= 0, porA= 0, totalA= 0, porB= 0, totalB= 0, porC= 0, totalC= 0, porD= 0, totalD= 0, porE= 0, totalE= 0, totalGp= 0, total = 0;

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
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getUnidadFuncional()), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(uf.getTitulo(), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(uf.getApellidoPropietario(), boldFontSmaller, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            // SALDOS UF
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalMesPrevio()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalPago), smallFonter, Element.ALIGN_RIGHT, PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getDeuda()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getIntereses()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));
            // TOTALES
            // A
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidad()), smallFonter, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalA()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // B
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadB()), smallFonter, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalB()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // C
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadC()), smallFonter, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalC()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // D
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadD()), smallFonter, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalD()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // E
            tablaEstadoCuenta.addCell(createCellEc(String.valueOf(uf.getPorcentajeUnidadE()), smallFonter, Element.ALIGN_CENTER,PdfPCell.BOX,null));
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalE()), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // GASTO PART
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(totalGastosParticulares), smallFonter, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // REDONDEO
//            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getRedondeo()), smallFont, Element.ALIGN_RIGHT,PdfPCell.BOX,null));

            // TOTAL A PAGAR
            tablaEstadoCuenta.addCell(createCellEc(formatoMoneda.format(estadoCuenta.getTotalExpensa()), boldFontSmaller, Element.ALIGN_RIGHT,PdfPCell.BOX,null));


        }
        document.add(tablaEstadoCuenta);

        PdfPTable tablaTotales = new PdfPTable(19); //
        tablaTotales.setWidthPercentage(100);
        tablaTotales.setWidths(new int[]{2,3,8 ,
                6,6,6,6,
                4,6,
                4,6,
                4,6,
                4,6,
                4,6,
                6,
                7});

        // Fila 1: Encabezados principales con celdas combinadas
        tablaTotales.addCell(createCellTotales("TOTALES", boldFontSubTitle, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground, 3));
        tablaTotales.addCell(createCell(formatoMoneda.format(saldo), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(pago), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(deuda), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(intereses), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(String.valueOf(redondear(porA,2)), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalA), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(String.valueOf(redondear(porB,2)), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalB), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(String.valueOf(redondear(porC,2)), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalC), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(String.valueOf(redondear(porD,2)), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalD), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(String.valueOf(redondear(porE,2)), boldFontSmaller, Element.ALIGN_CENTER, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalE), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(totalGp), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));
        tablaTotales.addCell(createCell(formatoMoneda.format(total), boldFontSmaller, Element.ALIGN_RIGHT, PdfPCell.BOX, grayBackground));

        document.add(tablaTotales);

//      FIN ESTADO CUENTA UNIDADES FUNCIONALES

//        document.setPageSize(PageSize.A4);
        document.newPage(); // <-- Salto de página
        document.close();

        return baos.toByteArray();
    }

    private static PdfPCell createCell(String content, Font font, int alignment, int border, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        return cell;
    }

    private static PdfPCell createCellEc(String content, Font font, int alignment, int border, BaseColor backgroundColor) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
        }
        cell.setPadding(2);

        return cell;
    }

    private static PdfPCell createCellTotales(String content, Font font, int alignment, int border, BaseColor backgroundColor, int colspan) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(border);
        if (backgroundColor != null) {
            cell.setBackgroundColor(backgroundColor);
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

    private static void agregarLogo(Document document, float ancho, float alto, float offsetY) {
        try (InputStream logoStream = PdfGenerator2.class.getResourceAsStream("/static/images/sumerios.png")) {
            if (logoStream != null) {
                Image logo = Image.getInstance(IOUtils.toByteArray(logoStream));
                logo.scaleToFit(ancho, alto);

                float x = (document.getPageSize().getWidth() - logo.getScaledWidth()) / 2;
                float y = document.getPageSize().getHeight() - logo.getScaledHeight() - offsetY;

                logo.setAbsolutePosition(x, y);
                document.add(logo);
            } else {
                System.out.println("No se encontró el logo.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
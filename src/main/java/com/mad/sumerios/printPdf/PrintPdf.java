package com.mad.sumerios.printPdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileNotFoundException;

public class PrintPdf {
    public static void printPdf(String pdfPath) throws Exception {
        File pdfFile = new File(pdfPath);

        // Validar si el archivo existe
        if (!pdfFile.exists()) {
            throw new FileNotFoundException("El archivo PDF no se encuentra: " + pdfPath);
        }

        try (PDDocument document = PDDocument.load(pdfFile)) {
            PrinterJob printerJob = PrinterJob.getPrinterJob();
            printerJob.setPageable(new PDFPageable(document));
            printerJob.print();
        }
    }
}

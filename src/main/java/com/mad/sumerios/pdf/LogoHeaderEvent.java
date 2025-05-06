package com.mad.sumerios.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

public class LogoHeaderEvent extends PdfPageEventHelper {

    private final Image logo;
    private final float offsetY;

    public LogoHeaderEvent(float ancho, float alto, float offsetY) throws Exception {
        try (InputStream logoStream = getClass().getResourceAsStream("/static/images/sumeriosOp.png")) {
            if (logoStream == null) {
                throw new Exception("No se encontró el logo en la ruta especificada.");
            }
            this.logo = Image.getInstance(IOUtils.toByteArray(logoStream));
            this.logo.scaleToFit(ancho, alto);
            this.offsetY = offsetY;
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte cb = writer.getDirectContent();
        float x = (document.getPageSize().getWidth() - logo.getScaledWidth()) / 2;
        float y = document.getPageSize().getHeight() - logo.getScaledHeight() - offsetY;
        logo.setAbsolutePosition(x, y);
        try {
            cb.addImage(logo);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}


package com.mad.sumerios.emailsender;

import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void enviarPagoPorCorreo(PagoUF pago,String nombreConsorcio, String pdfPath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            // Configurar los detalles del correo
            helper.setFrom(fromEmail);
            helper.setTo("maxii.drago@gmail.com");
            helper.setTo("sumerios.adm@gmail.com");
            if(pago.getUnidadFuncional().getMailPropietario() != null && !pago.getUnidadFuncional().getMailPropietario().isBlank()){
                helper.setTo(pago.getUnidadFuncional().getMailPropietario());
            }
            if(pago.getUnidadFuncional().getMailInquilino() != null && !pago.getUnidadFuncional().getMailInquilino().isBlank()){
                helper.setTo(pago.getUnidadFuncional().getMailInquilino());
            }

            helper.setSubject("Pago del Consorcio: "+nombreConsorcio+" - Unidad Funcional " + pago.getUnidadFuncional().getUnidadFuncional()+ " - " + pago.getUnidadFuncional().getTitulo());
            helper.setText("Estimado vecino,\n\nAdjunto encontrará el comprobante de su pago.\n\nSaludos,\nSumerios.");

            // Adjuntar el archivo PDF
            FileSystemResource file = new FileSystemResource(new File(pdfPath));
            helper.addAttachment("Comprobante_Pago.pdf", file);

            // Enviar el correo
            mailSender.send(message);

        } catch (Exception e) {
            throw new Exception("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}

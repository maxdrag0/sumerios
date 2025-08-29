package com.mad.sumerios.emailsender;

import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Component
public class EmailSender {
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void enviarPagoMailSumerios(UnidadFuncionalResponseDTO ufDto, String nombreConsorcio, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {

            // Configurar los detalles del correo
            helper.setFrom(fromEmail);
            helper.addBcc("sumerios.recibos@gmail.com");

            helper.setSubject("Pago del Consorcio: "+nombreConsorcio+" - Unidad Funcional " + ufDto.getUnidadFuncional()+ " - " + ufDto.getTitulo());
            helper.setText("Estimado vecino,\n\nAdjunto encontrará el comprobante de su pago.\n\nSaludos,\nSumerios.");


            // Adjuntar el archivo PDF

            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("Comprobante_Pago.pdf", resource);

            // Enviar el correo
            mailSender.send(message);

        } catch (Exception e) {
            throw new Exception("Error al enviar el correo: " + e.getMessage(), e);
        }
    }

    public void enviarPagoPorCorreo(List<String> mails, UnidadFuncionalResponseDTO ufDto, String nombreConsorcio, byte[] pdfBytes) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            // Configurar los detalles del correo
            helper.setFrom(fromEmail);
            helper.addBcc("sumerios.recibos@gmail.com");

            for (String mail : mails) {
                helper.addBcc(mail);
            }

            helper.setSubject("Pago del Consorcio: "+nombreConsorcio+" - Unidad Funcional " + ufDto.getUnidadFuncional()+ " - " + ufDto.getTitulo());
            helper.setText("Estimado vecino,\n\nAdjunto encontrará el comprobante de su pago.\n\nSaludos coordiales,\nSumerios.");

            // Adjuntar el archivo PDF
            ByteArrayResource resource = new ByteArrayResource(pdfBytes);
            helper.addAttachment("Comprobante_Pago.pdf", resource);

            // Enviar el correo
            mailSender.send(message);

        } catch (Exception e) {
            throw new Exception("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}

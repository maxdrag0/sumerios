package com.mad.sumerios.emailsender;

import com.mad.sumerios.movimientos.pagouf.model.PagoUF;
import com.mad.sumerios.unidadfuncional.dto.UnidadFuncionalResponseDTO;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void enviarPagoPorCorreo(List<String> mails, UnidadFuncionalResponseDTO ufDto, String nombreConsorcio, String pdfPath) throws Exception {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        try {
            File pdfFile = new File(pdfPath);
            if (!pdfFile.exists()) {
                throw new FileNotFoundException("El archivo PDF no se encuentra: " + pdfPath);
            }

            // Configurar los detalles del correo
            helper.setFrom(fromEmail);
            helper.addBcc("sumerios.recibos@gmail.com");

            for (String mail : mails) {
                helper.addBcc(mail);
            }

//            if(ufDto.getMailPropietario() != null && !ufDto.getMailPropietario().isBlank()){
//                helper.addBcc(ufDto.getMailPropietario());
//            }
//            if(ufDto.getMailInquilino() != null && !ufDto.getMailInquilino().isBlank()){
//                helper.addBcc(ufDto.getMailInquilino());
//            }

            helper.setSubject("Pago del Consorcio: "+nombreConsorcio+" - Unidad Funcional " + ufDto.getUnidadFuncional()+ " - " + ufDto.getTitulo());
            helper.setText("Estimado vecino,\n\nAdjunto encontrará el comprobante de su pago.\n\nSaludos,\nSumerios.");


            // Adjuntar el archivo PDF

            FileSystemResource file = new FileSystemResource(pdfFile);
            helper.addAttachment("Comprobante_Pago.pdf", file);

            // Enviar el correo
            mailSender.send(message);

        } catch (Exception e) {
            throw new Exception("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}

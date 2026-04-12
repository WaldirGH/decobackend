package com.deco.apideco.services;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.deco.apideco.model.Pedido;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarComprobante(Pedido pedido, byte[] pdf) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(pedido.getEmail());
            helper.setSubject("Tu comprobante de compra - Decoballoons");

            String cuerpo = """
                    Hola %s,

                    Gracias por tu compra en Decoballoons.
                    Adjuntamos tu comprobante en PDF.

                    Número de pedido: %s
                    Total: S/ %.2f

                    Atentamente,
                    Decoballoons
                    """.formatted(
                    pedido.getNombreCliente(),
                    pedido.getId(),
                    pedido.getTotal()
            );

            helper.setText(cuerpo);

            String nombreArchivo = "comprobante-pedido-" + pedido.getId() + ".pdf";
            helper.addAttachment(nombreArchivo, new ByteArrayResource(pdf));

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Error enviando correo con comprobante", e);
        }
    }
}

package system.cama.srl.Models.ServiceImpl;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Dao.VerificacionCorreoDao;
import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Entity.VerificacionCorreo;

@Service
public class VerificacionCorreoServiceImpl {

    @Autowired
    private VerificacionCorreoDao verificacionCorreoDao;

    @Autowired
    private JavaMailSender javaMailSender;

    @Transactional
    public void generarYEnviarToken(Usuario usuario, HttpServletRequest request) {
        String email = usuario.getPersona().getEmail();
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("El usuario no tiene un correo válido");
        }

        VerificacionCorreo existente = verificacionCorreoDao.findByUsuario(usuario);
        String token;
        LocalDateTime expiracion;

        if (existente != null) {
            // Limitar reenvío a máximo uno cada 5 minutos
            if (existente.getFechaUltimoEnvio() != null &&
                    existente.getFechaUltimoEnvio().isAfter(LocalDateTime.now().minusMinutes(5))) {
                throw new IllegalStateException("Ya se envió un correo recientemente. Intenta de nuevo más tarde.");
            }

            if (existente.getFechaExpiracion().isAfter(LocalDateTime.now())) {
                // Token vigente: reutilizar
                token = existente.getToken();
                expiracion = existente.getFechaExpiracion();
            } else {
                // Token expirado: eliminar y generar nuevo
                verificacionCorreoDao.delete(existente);
                token = UUID.randomUUID().toString();
                expiracion = LocalDateTime.now().plusHours(1);
                existente = new VerificacionCorreo();
                existente.setUsuario(usuario);
                existente.setToken(token);
                existente.setFechaExpiracion(expiracion);
            }
        } else {
            // No existe ningún token aún
            token = UUID.randomUUID().toString();
            expiracion = LocalDateTime.now().plusHours(1);
            existente = new VerificacionCorreo();
            existente.setUsuario(usuario);
            existente.setToken(token);
            existente.setFechaExpiracion(expiracion);
        }

        // Actualizar la fecha de último envío
        existente.setFechaUltimoEnvio(LocalDateTime.now());
        verificacionCorreoDao.save(existente);

        String urlBase = request.getScheme() + "://" + request.getServerName() +
                (request.getServerPort() != 80 && request.getServerPort() != 443 ? ":" + request.getServerPort() : "");
        String enlace = urlBase + "/verificar-email?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Verifica tu correo electrónico");
        message.setText("Haz clic en el siguiente enlace para verificar tu correo: " + enlace);

        javaMailSender.send(message);
    }
}

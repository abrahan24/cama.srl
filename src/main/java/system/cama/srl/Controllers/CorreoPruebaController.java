package system.cama.srl.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/correo")
public class CorreoPruebaController {

    @Autowired
    private JavaMailSender mailSender;

    // @GetMapping("/probar")
    // public ResponseEntity<String> enviarCorreoDePrueba() {
    //     try {
    //         SimpleMailMessage mensaje = new SimpleMailMessage();
    //         mensaje.setTo("saravia_25800_est@uap.edu.bo"); // Reemplaza con tu correo
    //         mensaje.setSubject("Prueba de envío de correo");
    //         mensaje.setText("Este es un correo de prueba enviado desde Spring Boot.");
    //         mailSender.send(mensaje);

    //         return ResponseEntity.ok("Correo enviado correctamente.");
    //     } catch (Exception e) {
    //         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
    //                              .body("Error al enviar correo: " + e.getMessage());
    //     }
    // }
}

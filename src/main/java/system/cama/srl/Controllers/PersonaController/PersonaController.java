package system.cama.srl.Controllers.PersonaController;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import system.cama.srl.Models.Dao.VerificacionCorreoDao;
import system.cama.srl.Models.Entity.Persona;
import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Entity.VerificacionCorreo;
import system.cama.srl.Models.Service.PersonaService;
import system.cama.srl.Models.Service.SubEnlaceService;
import system.cama.srl.Models.Service.UsuarioService;
import system.cama.srl.Models.ServiceImpl.VerificacionCorreoServiceImpl;

import org.springframework.web.bind.annotation.PostMapping;



@Controller
public class PersonaController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PersonaService personaService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private SubEnlaceService subEnlaceService;

    @Autowired
    private VerificacionCorreoServiceImpl verificacionCorreoServiceImpl;

    @Autowired
    private VerificacionCorreoDao verificacionCorreoDao;

    
    
    @GetMapping("/perfil_user")
    public String perfil_user(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") == null) {
            return "redirect:/login";
        }
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        model.addAttribute("enlaces", usuario.getRol().getEnlaces());
        model.addAttribute("subEnlaces", subEnlaceService.obtenerSubEnlacesPorIdUsuario(usuario.getId_usuario()));
        return "Persona/perfil_user";
    }

    @PostMapping("/seccionGeneral/{id_usuario}")
    public ResponseEntity<String> seccionGeneral(HttpServletRequest request,@PathVariable("id_usuario")Long id_usuario) {
         if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = usuarioService.findOne(id_usuario);
            Context context = new Context();
            context.setVariable("usuario", usuario); 
            String htmlContent = templateEngine.process("Persona/perfil_general", context);
            return ResponseEntity.ok(htmlContent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @PostMapping("/seccionInformacion/{id_usuario}")
    public ResponseEntity<String> seccionInformacion(HttpServletRequest request,@PathVariable("id_usuario")Long id_usuario) {
         if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = usuarioService.findOne(id_usuario);
            Context context = new Context();
            context.setVariable("usuario", usuario); 
            String htmlContent = templateEngine.process("Persona/perfil_informacion", context);
            return ResponseEntity.ok(htmlContent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @GetMapping("/navbar")
    public String getNavbar(HttpSession session, Model model) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return "redirect:/login"; // opcional si no hay usuario logueado
        }

        model.addAttribute("usuario", usuario);
        return "Layouts/Navbar :: navbar"; // tu fragmento
    }

      // Validar CI (evitar duplicado)
      @GetMapping("/validar_ci")
      public ResponseEntity<String> validarCI(@RequestParam String ci, @RequestParam(required = false) Long id) {
          Optional<Persona> persona = personaService.findByCi(ci);
  
          if (persona.isPresent()) {
              // Si está editando y es su mismo CI, no es duplicado
              if (id != null && persona.get().getId_persona().equals(id)) {
                  return ResponseEntity.ok("CI disponible.");
              }
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El CI ya está registrado.");
          }
  
          return ResponseEntity.ok("CI disponible.");
      }
  
      // Validar número de celular (evitar duplicado)
      @GetMapping("/validar_celular")
      public ResponseEntity<String> validarCelular(@RequestParam String celular, @RequestParam(required = false) Long id) {
          Optional<Persona> persona = personaService.findByNumCelular(celular);
  
          if (persona.isPresent()) {
              if (id != null && persona.get().getId_persona().equals(id)) {
                  return ResponseEntity.ok("Celular disponible.");
              }
              return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El número de celular ya está registrado.");
          }
  
          return ResponseEntity.ok("Celular disponible.");
      }

    @PostMapping("/actualizar_perfil_general")
    public ResponseEntity<String> actualizarPerfil(
            @RequestParam("user_name") String user_name,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("email") String email,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen,
            HttpSession session) {

        // Validar campos vacíos
        if (user_name.isBlank() || nombres.isBlank() || apellidos.isBlank() || email.isBlank()) {
            return ResponseEntity.badRequest().body("Todos los campos son obligatorios.");
        }

        // Validación para los campos nombres y apellidos (solo letras y espacios)
        String regexLetras = "^[A-Za-zÁÉÍÓÚáéíóúÑñ\\s]+$";
        if (!nombres.matches(regexLetras)) {
            return ResponseEntity.badRequest().body("El campo 'Nombres' solo debe contener letras y espacios.");
        }

        if (!apellidos.matches(regexLetras)) {
            return ResponseEntity.badRequest().body("El campo 'Apellidos' solo debe contener letras y espacios.");
        }

        // Validar el formato del correo
        String regexEmail = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(regexEmail)) {
            return ResponseEntity.badRequest().body("El correo electrónico tiene un formato inválido.");
        }

        // Obtener usuario en sesión
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe iniciar sesión.");
        }

        // Validación de imagen si fue enviada
        if (imagen != null && !imagen.isEmpty()) {
            String contentType = imagen.getContentType();
            if (!("image/jpeg".equals(contentType) || "image/png".equals(contentType))) {
                return ResponseEntity.badRequest().body("La imagen debe ser JPG o PNG.");
            }

            if (imagen.getSize() > 800 * 1024) { // 800 KB
                return ResponseEntity.badRequest().body("La imagen no debe superar los 800 KB.");
            }

            try {
                BufferedImage bufferedImage = ImageIO.read(imagen.getInputStream());
                if (bufferedImage == null) {
                    return ResponseEntity.badRequest().body("La imagen no es válida.");
                }

                int width = bufferedImage.getWidth();
                int height = bufferedImage.getHeight();

                if (width != 200 || height != 200) {
                    return ResponseEntity.badRequest().body("La imagen debe ser de 200x200 píxeles.");
                }

                byte[] imageBytes = imagen.getBytes();
                String base64Image = Base64.getEncoder().encodeToString(imageBytes);

                // Guardar imagen convertida en base64 (ejemplo)
                usuario.setImagen_base64(base64Image); // Asume que tienes un campo tipo String llamado `fotoPerfil`

            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al procesar la imagen.");
            }
        }

        // Guardar datos en el usuario
        usuario.setUser_name(user_name);
        Persona persona = usuario.getPersona();
        persona.setNombres(nombres);
        persona.setApellidos(apellidos);
        // Verificar si el email fue modificado
        String emailActual = persona.getEmail();
        boolean emailModificado = !emailActual.equalsIgnoreCase(email); // No distingue mayúsculas

        if (emailModificado) {
            System.out.println("El email ha sido modificado de " + emailActual + " a " + email);
            usuario.setCorreoVerificado(false); // Desactivar verificación si el correo fue cambiado
            // Aquí puedes agregar lógica adicional (enviar correo de verificación, log, etc.)
        }
        persona.setEmail(email);
        persona.setFecha_modificacion(new Date());
      
        // Guardar en base de datos
        personaService.save(persona);
        usuarioService.save(usuario);

        return ResponseEntity.ok("Perfil actualizado correctamente.");
    }

    @PostMapping("/cambiar_password")
    public ResponseEntity<String> cambiarPassword(
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("confirmPassword") String confirmPassword,
            HttpServletRequest request) {

        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe iniciar sesión.");
        }

        // Validar la contraseña antigua (texto plano)
        if (!usuario.getContrasena().equals(oldPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña actual es incorrecta.");
        }

        // Validar coincidencia de la nueva contraseña
        if (!newPassword.equals(confirmPassword)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Las nuevas contraseñas no coinciden.");
        }

        if (newPassword.length() < 6) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("La nueva contraseña debe tener al menos 6 caracteres.");
        }

        // Guardar nueva contraseña
        usuario.setContrasena(newPassword);
        usuarioService.save(usuario);

        return ResponseEntity.ok("Contraseña actualizada correctamente.");
    }
    
    @PostMapping("/reenviar-confirmacion")
    public ResponseEntity<String> reenviarConfirmacion(HttpSession session, HttpServletRequest request) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe iniciar sesión.");
        }

        if (usuario.isCorreoVerificado()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El correo ya fue verificado.");
        }

        verificacionCorreoServiceImpl.generarYEnviarToken(usuario, request);

        return ResponseEntity.ok("📧 Correo de verificación reenviado correctamente.");
    }

    @GetMapping("/verificar-email")
    public String verificarCorreo(@RequestParam("token") String token, RedirectAttributes attributes,
    HttpServletRequest request) {
        VerificacionCorreo verif = verificacionCorreoDao.findByToken(token);

        if (request.getSession().getAttribute("usuario") == null){
            return "redirect:/login";
        }

        if (verif == null || verif.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return "redirect:/perfil_user?verificado=error";
        }

        Usuario usuario = verif.getUsuario();
        usuario.setCorreoVerificado(true);
        usuarioService.save(usuario);
        verificacionCorreoDao.delete(verif);

        return "redirect:/perfil_user?verificado=ok";
    }

    @PostMapping("/reenviar-verificacion/{id}")
    public ResponseEntity<String> reenviarVerificacion(@PathVariable Long id, HttpServletRequest request) {
        try {
            Usuario usuario = usuarioService.findOne(id); // Asegúrate de tener este método
            verificacionCorreoServiceImpl.generarYEnviarToken(usuario, request);
            return ResponseEntity.ok("Correo de verificación reenviado.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado.");
        }
    }

    @PostMapping("/actualizar_persona")
    public ResponseEntity<String> actualizarPersona(@RequestParam(name = "id_persona")Long id_persona,
    @RequestParam(name = "fecha_nacimiento")@DateTimeFormat(pattern = "dd/MM/yyyy") Date fecha_nacimiento,
    @RequestParam(name = "num_celular") String num_celular,
    @RequestParam(name = "ci") String ci, HttpServletRequest request) {
        
        Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Debe iniciar sesión.");
        }

        try {
            // Buscar la persona original en la base de datos
            Persona persona = personaService.findOne(id_persona);
            if (persona != null) {
                
                persona.setFecha_nacimiento(fecha_nacimiento);
                persona.setNum_celular(num_celular);
                persona.setCi(ci);
                personaService.save(persona);

                return ResponseEntity.ok("Datos actualizados correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Persona no encontrada.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar datos.");
        }
    }
}

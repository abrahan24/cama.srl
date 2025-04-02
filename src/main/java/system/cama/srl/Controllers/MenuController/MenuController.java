package system.cama.srl.Controllers.MenuController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Service.EnlaceService;
import system.cama.srl.Models.Service.SubEnlaceService;
import system.cama.srl.Models.Service.UsuarioService;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;



@Controller
public class MenuController {
    
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EnlaceService enlaceService;

    @Autowired
    private SubEnlaceService subEnlaceService;

    @Autowired
    private TemplateEngine templateEngine;


    @GetMapping("/menu")
    public String menuPrincipal_Vista(Model model,HttpServletRequest request) {

        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
            model.addAttribute("enlaces", usuario.getRol().getEnlaces());
            model.addAttribute("subEnlaces", subEnlaceService.obtenerSubEnlacesPorIdUsuario(usuario.getId_usuario()));
            return "MenuPrincipal/MenuPrincipal";
        }else{
            return "redirect:/login";

        }

    }

    // @PostMapping("/cargarMenu")
    // public ResponseEntity<Map<String, Object>> cargarMenu(HttpServletRequest request) {

    //     if (request.getSession().getAttribute("usuario") != null) {
    //         Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");
    //         Usuario u = usuarioService.findOne(usuario.getId_usuario());
    
    //         // Obtener los enlaces
    //         List<Enlace> listaEnlaces = enlaceService.obtenerEnlacesPorIdUsuario(u.getId_usuario());
    
    //         List<SubEnlace> listaSubEnlaces = subEnlaceService.obtenerSubEnlacesPorIdUsuario(u.getId_usuario());
    
    //         Map<String, Object> respuesta = new HashMap<>();
    //         respuesta.put("enlaces", listaEnlaces);
    //         respuesta.put("subEnlaces", listaSubEnlaces);
    //         respuesta.put("status", "1");
    
    //         return ResponseEntity.ok(respuesta);

    //     } else {
    //         return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
    //                 .body(Collections.singletonMap("error", "Usuario no autenticado"));

    //     }

    // }

    @PostMapping("/cargarMenuEnlaces")
    public ResponseEntity<String> cargarMenuEnlaces(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Usuario usuario = (Usuario) request.getSession().getAttribute("usuario");

            model.addAttribute("enlaces", usuario.getRol().getEnlaces());
            model.addAttribute("subEnlaces", subEnlaceService.obtenerSubEnlacesPorIdUsuario(usuario.getId_usuario()));

            // Renderizar el fragmento Thymeleaf
            String htmlFragment = renderThymeleafTemplate(model, "Layouts/MainMenu");

            return ResponseEntity.ok(htmlFragment);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("<p>Usuario no autenticado</p>");
        }
    }
    

    private String renderThymeleafTemplate(Model model, String templateName) {
        Context context = new Context();
        model.asMap().forEach(context::setVariable);
        return templateEngine.process(templateName, context);
    }
}

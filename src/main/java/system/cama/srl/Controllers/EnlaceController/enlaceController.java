package system.cama.srl.Controllers.EnlaceController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Entity.Enlace;
import system.cama.srl.Models.Entity.Rol;
import system.cama.srl.Models.Entity.SubEnlace;
import system.cama.srl.Models.Service.EnlaceService;
import system.cama.srl.Models.Service.RolService;
import system.cama.srl.Models.Service.SubEnlaceService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class enlaceController {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private RolService rolService;

    @Autowired
    private EnlaceService enlaceService;

    @Autowired
    private SubEnlaceService subEnlaceService;
    
    @GetMapping("/gestionEnlaces")
    public String gestionEnlaces(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("roles", rolService.findAll());

            return "Enlace/enlace";
        }else{
            return "redirect:/login";

        }
    }
    
    @PostMapping("/cargarFormRol")
    public ResponseEntity<String> cargarFormRol(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Context context = new Context();
            context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
            String htmlContent = templateEngine.process("Content/modalRol", context);
            return ResponseEntity.ok(htmlContent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @PostMapping("/cargarFormEnlace")
    public ResponseEntity<String> cargarFormEnlace(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Context context = new Context();
            context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
            context.setVariable("roles", rolService.findAll());
            String htmlContent = templateEngine.process("Content/modalEnlace", context);
            return ResponseEntity.ok(htmlContent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @PostMapping("/cargarFormSubEnlace")
    public ResponseEntity<String> cargarFormSubEnlace(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {
            Context context = new Context();
            context.setVariable("usuario", request.getSession().getAttribute("usuario"));
            context.setVariable("enlaces", enlaceService.findAll()); 
            String htmlContent = templateEngine.process("Content/modalSubEnlace", context);
            return ResponseEntity.ok(htmlContent);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    

    @PostMapping("/registrarRol")
    public ResponseEntity<String> registrarRol(@RequestParam(name = "nom_rol",required = true) String nom_rol,
    @RequestParam(name = "descripcion",required = false)String descripcion, HttpServletRequest request) {
   
        if (request.getSession().getAttribute("usuario") != null) {

            Rol rol = new Rol();
            rol.setNom_rol(nom_rol);
            rol.setDescripcion(descripcion);
            rol.setEstado_rol("A");
            rolService.save(rol);

            return ResponseEntity.ok("1");

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");

        }
    }
    
    @PostMapping("/cagarTabla")
    public ResponseEntity< String >cagarTabla(HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("roles", rolService.findAll()); 
            String htmlContent = templateEngine.process("Enlace/tablaEnlace", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    
    @PostMapping("/registrarEnlace")
    public ResponseEntity<String> registrarEnlace(@RequestParam(name = "nom_enlace",required = true) String nom_enlace,
    @RequestParam(name = "ruta_enlace",required = false)String ruta_enlace,
    @RequestParam(name = "icono_enlace")String icono_enlace,
    @RequestParam(name = "id_rol")Long id_rol, HttpServletRequest request) {
   
        if (request.getSession().getAttribute("usuario") != null) {

           Enlace enlace = new Enlace();

           enlace.setRol(rolService.findOne(id_rol));
           enlace.setNom_enlace(nom_enlace);
           enlace.setRuta_enlace(ruta_enlace);
           enlace.setIcono_enlace(icono_enlace);
           enlace.setEstado_enlace("A");
           enlaceService.save(enlace);

            return ResponseEntity.ok("1");

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");

        }
    }

    @PostMapping("/registrarSubEnlace")
    public ResponseEntity<String> registrarSubEnlace(@RequestParam(name = "id_enlace",required = true) Long id_enlace,
    @RequestParam(name = "nom_subenlace",required = false)String nom_subenlace,
    @RequestParam(name = "ruta_subenlace")String ruta_subenlace,
    @RequestParam(name = "icono_subenlace")String icono_subenlace, HttpServletRequest request) {
   
        if (request.getSession().getAttribute("usuario") != null) {

            SubEnlace subEnlace = new SubEnlace();

            subEnlace.setEnlace(enlaceService.findOne(id_enlace));
            subEnlace.setEstado_sub_enlace("A");
            subEnlace.setIcono_subenlace(icono_subenlace);
            subEnlace.setRuta_subenlace(ruta_subenlace);
            subEnlace.setNom_sub_enlace(nom_subenlace);
            subEnlaceService.save(subEnlace);

            return ResponseEntity.ok("1");

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");

        }
    }

}

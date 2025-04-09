package system.cama.srl.Controllers.LoginController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import system.cama.srl.Models.Entity.Usuario;
import system.cama.srl.Models.Service.UsuarioService;



@Controller
public class loginController {

    @Autowired
    private UsuarioService usuarioService;
    
    @GetMapping("/")
    public String getMethodName() {
        return "Login/login";
    }
    

    @GetMapping("/login")
    public String vista_Login() {

        return "Login/login";
    }
    
    @PostMapping("/loginPost")
    public ResponseEntity<String> formLoginPost(@RequestParam(name = "user") String user,
            @RequestParam(name = "password") String password, HttpServletRequest request) {

        Usuario usuario = usuarioService.getUsuario(user, password);
        if (usuario != null) {

            if (usuario.getEstado_usuario().equals("X")) {
                return ResponseEntity.ok("3");
            }

            HttpSession session = request.getSession(true);

            session.setAttribute("usuario", usuario);
            session.setAttribute("persona", usuario.getPersona());
            session.setAttribute("rol", usuario.getRol());
            return ResponseEntity.ok("1");

        } else {

            System.out.println("no recuperado");
            return ResponseEntity.ok("2");

        }

    }

    @GetMapping("/cerrar_sesion")
    public String getMethodName(HttpServletRequest request, RedirectAttributes flash) {
        HttpSession session = request.getSession();
        if (session != null) {
            session.invalidate();
            flash.addAttribute("mensaje", "Se Cerro la Sesion Con Exito");
        }
        return "redirect:/login";
    }
    
}

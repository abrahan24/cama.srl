package system.cama.srl.Controllers.VentaController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Service.VentaService;

@Controller
public class VentaController {
    

    @Autowired
    private VentaService ventaService;

    @GetMapping("/registroVenta")
    public String registro_venta(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

           

            return "Venta/venta";
        } else {
            return "redirect:/login";

        }
    }

    
}

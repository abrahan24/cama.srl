package system.cama.srl.Controllers.VentaController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Entity.Cliente;
import system.cama.srl.Models.Service.ClienteService;
import system.cama.srl.Models.Service.VentaService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;


@Controller
public class VentaController {
    
    @Autowired
    private VentaService ventaService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/registroVenta")
    public String registro_venta(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            return "Venta/venta";
        } else {
            return "redirect:/login";

        }
    }

    @PostMapping("/manejarTipoCliente/{tipo_cliente}")
    public ResponseEntity<String> manejarTipoCliente(HttpServletRequest request,@PathVariable(name = "tipo_cliente") String tipo_cliente) {
         if (request.getSession().getAttribute("usuario") != null) {
            
            if (tipo_cliente.equals("N")) {
                Context context = new Context();
                context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
                String htmlContent = templateEngine.process("Venta/clienteNuevo", context);
                return ResponseEntity.ok(htmlContent);

            }else{
                Context context = new Context();
                context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
                String htmlContent = templateEngine.process("Venta/clienteAntiguo", context);
                return ResponseEntity.ok(htmlContent);
            }
           
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    
    @PostMapping("/obtenerClientesPorNombres/{nombres}")
    public ResponseEntity<?> obtenerClientesPorNombres(HttpServletRequest request,
        @PathVariable("nombres") String nombres) {
        
        if (request.getSession().getAttribute("usuario") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
        
        Context context = new Context();
        context.setVariable("clientes", clienteService.findByNombresPersonaContaining(nombres)); 
        String htmlContent = templateEngine.process("Venta/tablaClienteAntiguo", context);
        return ResponseEntity.ok(htmlContent);
    }
    
    @PostMapping("/obtenerClientesPorNit/{nit}")
    public ResponseEntity<?> obtenerClientesPorNit(HttpServletRequest request,
        @PathVariable("nit") String nit) {
        
        if (request.getSession().getAttribute("usuario") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
        
        Context context = new Context();
        context.setVariable("clientes", clienteService.findByNitContaining(nit)); 
        String htmlContent = templateEngine.process("Venta/tablaClienteAntiguo", context);
        return ResponseEntity.ok(htmlContent);
    }

    @PostMapping("/obtenerClientesPorCi/{ci}")
    public ResponseEntity<?> obtenerClientesPorCi(HttpServletRequest request,
        @PathVariable("ci") String ci) {
        
        if (request.getSession().getAttribute("usuario") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
        
        Context context = new Context();
        context.setVariable("clientes", clienteService.findByCiPersonaContaining(ci)); 
        String htmlContent = templateEngine.process("Venta/tablaClienteAntiguo", context);
        return ResponseEntity.ok(htmlContent);
    }

    @PostMapping("/registrarVentaClienteAntiguo")
    public String registrarVentaClienteAntiguo(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/RegistrarVentaClienteNuevo")
    public String RegistrarVentaClienteNuevo(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
}

package system.cama.srl.Controllers.ProformaController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Service.ProductoService;


@Controller
public class ProformaController {

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private ProductoService productoService;
    
    @PostMapping("/manejarTipoProforma/{tipo_proforma}")
    public ResponseEntity< String >postMethodName(@PathVariable(name = "tipo_proforma") String tipo_proforma,HttpServletRequest request) {
       if (request.getSession().getAttribute("usuario") != null) {
            
            if (tipo_proforma.equals("CMO")) {
                Context context = new Context();
                context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
                String htmlContent = templateEngine.process("Proforma/conManoDeObra", context);
                return ResponseEntity.ok(htmlContent);

            }else{
                Context context = new Context();
                context.setVariable("usuario", request.getSession().getAttribute("usuario")); 
                String htmlContent = templateEngine.process("Proforma/sinManoDeObra", context);
                return ResponseEntity.ok(htmlContent);
            }
           
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    
    @PostMapping("/cagarTablaProformaPorNombreProducto/{nom_producto}")
    public ResponseEntity< String >cagarTablaProformaPorNombreProducto(HttpServletRequest request,
    @PathVariable("nom_producto") String nom_producto) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("productos", productoService.obtenerProductosPorNombres(nom_producto)); 
            String htmlContent = templateEngine.process("Proforma/tablaProductosEncontrados", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    
    @PostMapping("/cagarTablaProformaPorCodigoProducto/{nom_producto}")
    public ResponseEntity< String >cagarTablaProformaPorCodigoProducto(HttpServletRequest request,
    @PathVariable("nom_producto") String nom_producto) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("productos", productoService.obtenerProductosPorNombres(nom_producto)); 
            String htmlContent = templateEngine.process("Proforma/tablaProductosSeleccionados", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }
    
}

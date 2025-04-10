package system.cama.srl.Controllers.ProductoController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.servlet.http.HttpServletRequest;
import system.cama.srl.Models.Entity.Enlace;
import system.cama.srl.Models.Entity.Producto;
import system.cama.srl.Models.Service.ProductoService;
import system.cama.srl.Models.Service.TipoProductoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


@Controller
public class ProductoController {
    
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private TipoProductoService tipoProductoService;

    @Autowired
    private ProductoService productoService;
    

    @GetMapping("/gestionProductos")
    public String gestionProductos(Model model, HttpServletRequest request) {
        if (request.getSession().getAttribute("usuario") != null) {

            model.addAttribute("tiposP", tipoProductoService.findAll());
            return "Producto/producto";
        } else {
            return "redirect:/login";

        }
    }

    @PostMapping("/registrarProductoNuevo")
    public ResponseEntity<String> registrarProducto(
            HttpServletRequest request,
            @RequestParam("nom_producto") String nom_producto,
            @RequestParam("cod_producto") String cod_producto,
            @RequestParam("stock") Double stock,
            @RequestParam("precio") Double precio,
            @RequestParam("marca") String marca,
            @RequestParam("descripcion_producto") String descripcion_producto,
            @RequestParam("id_tipo_producto") Long id_tipo_producto,
            @RequestParam("file") MultipartFile file) {

        // Validar sesión
        if (request.getSession().getAttribute("usuario") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }

        try {
            Producto producto = new Producto();
            producto.setNom_producto(nom_producto.toUpperCase());
            producto.setCod_producto(cod_producto.toUpperCase());
            producto.setStock(stock);
            producto.setPrecio(precio);
            producto.setMarca(marca.toUpperCase());
            producto.setDescripcion_producto(descripcion_producto.toUpperCase());
            producto.setTipoProducto(tipoProductoService.findOne(id_tipo_producto));
            producto.setEstado_producto("A");
            producto.setFecha_registro(new Date());
            producto.setUser_mod(null);
            // Procesar la imagen
            if (!file.isEmpty()) {
                // 1. Usar ruta absoluta o directorio externo (recomendado para producción)
                String uploadDir = System.getProperty("user.dir") + "/uploads/";
                // Alternativa para desarrollo:
                // String uploadDir = "C:/uploads/"; // Windows
                // String uploadDir = "/var/uploads/"; // Linux/Mac

                // 2. Crear directorio si no existe
                Path uploadPath = Paths.get(uploadDir);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                // 3. Generar nombre único y seguro para el archivo
                String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
                String fileName = UUID.randomUUID().toString() + fileExtension;

                // 4. Guardar el archivo (usando java.nio.Path)
                Path filePath = uploadPath.resolve(fileName);
                file.transferTo(filePath.toFile());

                // 5. Guardar ruta relativa o absoluta en BD
                producto.setRuta_imagen(fileName); // Ruta accesible desde el frontend
            }

            productoService.save(producto);
            return ResponseEntity.ok("1");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado: " + e.getMessage());
        }
    }
    
  
    @PostMapping("/cagarTablaProductosPorNombreProducto/{nom_producto}")
    public ResponseEntity< String >cagarTablaProductosPorNombreProducto(HttpServletRequest request,
    @PathVariable("nom_producto") String nom_producto) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("productos", productoService.obtenerProductosPorNombres(nom_producto)); 
            String htmlContent = templateEngine.process("Producto/tablaProducto", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @PostMapping("/cagarTablaProductosPorCodigoProducto/{cod_producto}")
    public ResponseEntity< String >cagarTablaProductosPorCodigoProducto(HttpServletRequest request,
    @PathVariable("cod_producto") String cod_producto) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("productos", productoService.obtenerProductosPorCodigoProducto(cod_producto)); 
            String htmlContent = templateEngine.process("Producto/tablaProducto", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

}

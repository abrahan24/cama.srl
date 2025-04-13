package system.cama.srl.Controllers.ProformaController;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.*;
import system.cama.srl.Models.Entity.Producto;
import system.cama.srl.Models.Service.ProductoService;
import org.springframework.core.io.Resource; // Añade esta importación
import org.springframework.core.io.ClassPathResource; // Y esta también
import net.sf.jasperreports.engine.export.JRPdfExporter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;


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
    
    @PostMapping("/cagarTablaProformaPorCodigoProducto/{cod_producto}")
    public ResponseEntity< String >cagarTablaProformaPorCodigoProducto(HttpServletRequest request,
    @PathVariable("cod_producto") String cod_producto) {
        if (request.getSession().getAttribute("usuario") != null) {

            Context context = new Context();
            context.setVariable("productos", productoService.obtenerProductosPorCodigoProducto(cod_producto)); 
            String htmlContent = templateEngine.process("Proforma/tablaProductosEncontrados", context);

            return ResponseEntity.ok(htmlContent);

        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acceso denegado");
        }
    }

    @PostMapping("/realizarProforma")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/generarProformaConManoDeObra")
    public ResponseEntity<byte[]> generarProformaConManoDeObra(
            @RequestParam String nom_cliente,
            @RequestParam String mano_obra_json,
            @RequestParam String productos_json) {

        try {
            // Validación básica de parámetros
            if (nom_cliente == null || nom_cliente.trim().isEmpty()) {
                throw new IllegalArgumentException("El nombre del cliente es requerido");
            }

            // Convertir JSON a objetos Java
            ObjectMapper objectMapper = new ObjectMapper();
            List<Map<String, Object>> manoObraList = objectMapper.readValue(mano_obra_json,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> productosList = objectMapper.readValue(productos_json,
                    new TypeReference<List<Map<String, Object>>>() {
                    });

            // Validar listas no vacías
            if (manoObraList.isEmpty() && productosList.isEmpty()) {
                throw new IllegalArgumentException("Debe incluir al menos un item de mano de obra o productos");
            }

            // Preparar parámetros para el reporte
            Map<String, Object> params = new HashMap<>();
            params.put("NOM_CLIENTE", nom_cliente);
            params.put("FECHA", new Date());

            // Crear DataSources
            JRBeanCollectionDataSource manoObraDS = new JRBeanCollectionDataSource(manoObraList);
            JRBeanCollectionDataSource productosDS = new JRBeanCollectionDataSource(productosList);

            params.put("MANO_OBRA_DS", manoObraDS);
            params.put("PRODUCTOS_DS", productosDS);

            // Calcular totales con manejo de nulos
            BigDecimal totalManoObra = manoObraList.stream()
                    .map(m -> new BigDecimal(m.getOrDefault("precio", "0").toString()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal totalProductos = productosList.stream()
                    .map(p -> {
                        BigDecimal precio = new BigDecimal(p.getOrDefault("precio", "0").toString());
                        BigDecimal cantidad = new BigDecimal(p.getOrDefault("cantidad", "1").toString());
                        return precio.multiply(cantidad);
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            params.put("TOTAL_MANO_OBRA", totalManoObra);
            params.put("TOTAL_PRODUCTOS", totalProductos);
            params.put("TOTAL_GENERAL", totalManoObra.add(totalProductos));

            // Obtener ruta base del proyecto
            Path rootPath = Paths.get("");
            Path rootAbsolutPath = rootPath.toAbsolutePath();

            // Definir rutas posibles para el reporte
            String[] possibleSubPaths = {
                    "src/main/resources/Report/Proformas/proforma.jrxml",
                    "Report/Proformas/proforma.jrxml",
                    "../Report/Proformas/proforma.jrxml"
            };

            InputStream reportStream = null;
            String foundPath = null;

            // Buscar el archivo en las ubicaciones posibles
            for (String subPath : possibleSubPaths) {
                String fullPath = rootAbsolutPath.toString() + "/" + subPath;
                File file = new File(fullPath);
                if (file.exists()) {
                    foundPath = fullPath;
                    reportStream = new FileInputStream(file);
                    break;
                }
            }

            if (reportStream == null) {
                throw new FileNotFoundException(
                        "No se encontró el archivo proforma.jrxml en ninguna ubicación conocida");
            }

            // Generar el PDF
            try (InputStream finalStream = reportStream) {
                System.out.println("Generando reporte desde: " + foundPath);

                JasperReport jasperReport = JasperCompileManager.compileReport(finalStream);
                JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);

                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=proforma.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(outputStream.toByteArray());
            }

        } catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error en el formato JSON: " + e.getMessage());
        } catch (FileNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Archivo de reporte no encontrado: " + e.getMessage());
        } catch (JRException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error al generar el PDF: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error inesperado: " + e.getMessage());
        }
    }
}
package system.cama.srl;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer{
    
     @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Ruta absoluta al directorio 'uploads' (ajusta según tu sistema operativo)
        String uploadsDir = System.getProperty("user.dir") + "/uploads/";
        
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadsDir);
    }
}

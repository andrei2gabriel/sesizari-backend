package ro.botosani.ticketing_backend.config; // Ajustează pachetul dacă e diferit

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Obținem calea absolută a folderului "uploads" de pe HDD și o transformăm în format URI (file://...)
        String uploadPath = Paths.get(System.getProperty("user.dir"), "uploads").toUri().toString();

        // Orice cerere către localhost:8080/uploads/.... va fi direcționată către acel folder fizic
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadPath);
    }
}
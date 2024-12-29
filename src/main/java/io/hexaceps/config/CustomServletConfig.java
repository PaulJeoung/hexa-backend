package io.hexaceps.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class CustomServletConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        log.info("Adding CORS mappings 동작 중");
        registry.addMapping("/**")
                .maxAge(1000)
                .allowedMethods("GET", "POST", "PUT", "OPTIONS")
                .allowedOrigins("*");
    }
}

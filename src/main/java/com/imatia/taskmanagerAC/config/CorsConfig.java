package com.imatia.taskmanagerAC.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for enabling and customizing CORS (Cross-Origin Resource Sharing) settings.
 */
@Configuration
public class CorsConfig implements WebMvcConfigurer {

    /**
     * Configures the CORS mappings for the application.
     *
     * <p>Allows cross-origin requests for all endpoints (/**) with the following settings:</p>
     * <ul>
     *     <li>Allowed origins: All (*)</li>
     *     <li>Allowed HTTP methods: GET, POST, PUT, DELETE, OPTIONS, PATCH</li>
     *     <li>Allowed headers: All (*)</li>
     *     <li>Credentials: Not allowed (false)</li>
     * </ul>
     *
     * @param registry the {@link CorsRegistry} to configure.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(false);
    }
}


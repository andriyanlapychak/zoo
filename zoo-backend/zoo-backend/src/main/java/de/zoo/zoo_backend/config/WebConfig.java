package de.zoo.zoo_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Technische Konfiguration für den Webzugriff (keine Fachlogik).
 * Erlaubt dem Angular-Frontend, die API aus dem Browser aufzurufen (CORS).
 */
@Configuration // Spring liest diese Klasse beim Start und wendet die Einstellungen an
public class WebConfig implements WebMvcConfigurer {

    /**
     * Legt fest, welche fremden Origins die API im Browser nutzen dürfen.
     * Der Browser fragt bei PUT und DELETE vorab mit einer OPTIONS-Anfrage nach (Preflight).
     * Spring beantwortet sie anhand dieser Regeln selbst.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")                     // gilt für alle Endpunkte unter /api
                .allowedOrigins("http://localhost:4200")   // nur unser Angular-Entwicklungsserver
                .allowedMethods("GET", "POST", "PUT", "DELETE"); // erlaubte HTTP-Methoden
    }
}
package io.github.hison.api.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.github.hison.api.util.CorsValidator;
import jakarta.annotation.PostConstruct;

import java.util.List;

/** 
 * ApiLinkController with dynamic CORS configuration using WebMvcConfigurer.
 * 
 * @author Hani Son
 * @version 2.0.0
 */
@RestController
@RequestMapping("${hison.link.api.path:/hison-api-link}")
@ConditionalOnMissingBean(ApiLink.class)
public class ApiLinkController extends ApiLink implements WebMvcConfigurer {

    @Value("${hison.link.api.cors.origins:*}")
    private String corsOrigins;

    @Value("${hison.link.api.cors.allow-credentials:false}")
    private boolean allowCredentials;

    @Value("${hison.link.api.cors.methods:GET,POST,PUT,PATCH,DELETE,HEAD,OPTIONS}")
    private String corsMethods;

    @Value("${hison.link.api.status.message:Hison API is running.}")
    private String statusMessage;

    @PostConstruct
    public void validateCorsConfig() {
        List<String> origins = CorsValidator.parseOrigins(corsOrigins);
        CorsValidator.validateCorsSettings(origins, allowCredentials);
    }

    @RequestMapping("/status")
    public String status() {
        return statusMessage;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        List<String> origins = CorsValidator.parseOrigins(corsOrigins);
        List<String> methods = CorsValidator.parseOrigins(corsMethods);

        registry.addMapping("/**")
                .allowedOrigins(origins.toArray(new String[0]))
                .allowedMethods(methods.toArray(new String[0]))
                .allowCredentials(allowCredentials);
    }
}

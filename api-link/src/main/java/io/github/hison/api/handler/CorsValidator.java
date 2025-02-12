package io.github.hison.api.handler;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/** 
 * @author Hani son
 * @version 1.0.3
 */
@Component
public class CorsValidator {

    public List<String> parseOrigins(String corsOrigins) {
        return Arrays.asList(corsOrigins.split(","));
    }

    public void validateCorsSettings(List<String> origins, boolean allowCredentials) {
        if (allowCredentials && origins.contains("*")) {
            throw new IllegalArgumentException("CORS configuration error: When allowCredentials=true, origins cannot contain '*'. Please specify specific domains.");
        }
    }
}
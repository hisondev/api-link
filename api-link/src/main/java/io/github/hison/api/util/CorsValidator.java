package io.github.hison.api.util;

import java.util.Arrays;
import java.util.List;

/** 
 * Utility class for CORS validation.
 * Provides static methods for parsing origins and validating CORS settings.
 * 
 * @author Hani Son
 * @version 1.0.7
 */
public class CorsValidator {

    // Prevent instantiation
    private CorsValidator() {}

    public static List<String> parseOrigins(String corsOrigins) {
        return Arrays.asList(corsOrigins.split(","));
    }

    public static void validateCorsSettings(List<String> origins, boolean allowCredentials) {
        if (allowCredentials && origins.contains("*")) {
            throw new IllegalArgumentException("CORS configuration error: When allowCredentials=true, origins cannot contain '*'. Please specify specific domains.");
        }
    }
}

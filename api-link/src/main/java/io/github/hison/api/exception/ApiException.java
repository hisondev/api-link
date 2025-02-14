package io.github.hison.api.exception;

/**
 * 
 * Custom exception class for handling API-related exceptions, particularly used within the ApiController.
 * This exception extends the standard RuntimeException and provides additional functionality
 * to include an error code with the exception message. It is designed to catch systemic errors
 * occurring within the ApiController and then be passed to the ApiHandler's handleApiException method.
 * 
 * <p>Key Features:</p>
 * <ul>
 * <li>Standard exception message handling with the addition of an error code.</li>
 * <li>Integrates with ApiHandler to allow structured error handling in API operations.</li>
 * </ul>
 * <p>Usage and Handling:</p>
 * <pre>
 *     // Throwing an ApiException
 *     throw new ApiException("Error processing request", "APIERROR001");
 *     
 *     // Handling in ApiHandler
 *     public ResponseEntity&lt;DataWrapper&gt; handleApiException(ApiException e, DataWrapper dw, HttpServletRequest req) {
 *         // Custom error logic here
 *     }
 * </pre>
 * 
 * ApiException is vital for structured error handling in API operations, enabling developers to
 * write custom error logic within ApiHandler's handleApiException method.
 * 
 * @author Hani son
 * @version 1.0.6
 */
public class ApiException extends RuntimeException {
    private String code = "APIERROR";
    
    public ApiException(String message) {
        super(message);
    }

    public ApiException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

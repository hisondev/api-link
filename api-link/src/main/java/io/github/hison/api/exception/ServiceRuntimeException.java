package io.github.hison.api.exception;

/**
 * 
 * Custom exception class for handling exceptions that occur within service layer operations.
 * Extends RuntimeException and includes an additional error code for more detailed exception categorization.
 * This class is designed to be used in conjunction with the ApiHandler's handleServiceRuntimeException method,
 * allowing for customizable catch logic.
 * 
 * <p>Key Features:</p>
 * <ul>
 * <li>Standard runtime exception handling with the addition of an error code.</li>
 * <li>Enables structured error handling in service layer operations.</li>
 * <li>Integrates seamlessly with ApiController, automatically redirecting to ApiHandler's handleServiceRuntimeException for error processing.</li>
 * </ul>
 * <p>Usage and Integration:</p>
 * <pre>
 *     // Throwing a ServiceRuntimeException in service layer
 *     &#64;Service
 *     public class MemberService {
 *         public DataWrapper getMember(DataWrapper dw) {
 *             throw new ServiceRuntimeException("This is Runtime exception message test", "ERR0001");
 *         }
 *         ...
 *     }
 * 
 *     // Handling in ApiHandler
 *     public ResponseEntity&lt;DataWrapper&gt; handleServiceRuntimeException(ServiceRuntimeException e, DataWrapper dw, HttpServletRequest req) {
 *         // Custom catch logic here
 *     }
 * </pre>
 * 
 * ServiceRuntimeException is vital for a structured approach to handling errors in the service layer,
 * providing a clear path for managing exceptions and enhancing error response mechanisms in API operations.
 * 
 * @author Hani son
 * @version 1.0.6
 */
public class ServiceRuntimeException extends RuntimeException {
    private String code = "0000";

    public ServiceRuntimeException(String message) {
        super(message);
    }

    public ServiceRuntimeException(String message, String code) {
        super(message);
        this.code = code;
    }

    public ServiceRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRuntimeException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public ServiceRuntimeException(Throwable cause) {
        super(cause.toString(), cause);
    }

    public ServiceRuntimeException(Throwable cause, String code) {
        super(cause.toString(), cause);
        this.code = code;
    }

    public ServiceRuntimeException(ServiceRuntimeException cause) {
        super(cause.toString(), cause);
    }

    public ServiceRuntimeException(ServiceRuntimeException cause, String code) {
        super(cause.toString(), cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}

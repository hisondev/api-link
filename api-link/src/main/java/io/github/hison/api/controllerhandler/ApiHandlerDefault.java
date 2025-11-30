package io.github.hison.api.controllerhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import io.github.hison.api.exception.ApiException;
import io.github.hison.api.exception.ServiceRuntimeException;
import io.github.hison.data.model.DataModel;
import io.github.hison.data.wrapper.DataWrapper;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Default implementation of the ApiHandler interface.
 * This class provides basic handling for API requests and exceptions, and can be extended for custom behavior.
 * It is part of a factory pattern implemented in ApiHandlerFactory, enabling developers to customize API handling logic.
 * 
 * <p>Key Features:</p>
 * <ul>
 * <li>Implements essential API handling methods such as beforeHandleRequest, handleAuthority, handleLog, and afterHandleRequest.</li>
 * <li>Provides standard responses for ApiException, ServiceRuntimeException, generic Exception, and Throwable.</li>
 * </ul>
 * <p>Customization:</p>
 * 
 * Developers can create a custom handler, such as CustomApiHandler, extending ApiHandlerDefault to override and implement custom logic.
 * This custom handler can be registered in the application startup:
 * 
 * <pre>
 *     &#64;SpringBootApplication
 *     public class DemoApplication {
 *         public static void main(String[] args) {
 *             CustomApiHandler.register();
 *         }
 *     }
 * </pre>
 * ApiHandlerFactory facilitates this customization by returning the custom handler when set, otherwise defaulting to ApiHandlerDefault.
 * <p>Usage Example:</p>
 * <pre>
 *     public class CustomApiHandler extends ApiHandlerDefault {
 *         public static void register() {
 *              ApiHandlerFactory.setCustomHandler(new CustomApiHandler());
 *         }
 *         // Override methods for custom behavior
 *     }
 * </pre>
 * ApiHandlerDefault is essential for handling API requests and providing a structured approach to error handling in API operations.
 * 
 * @author Hani son
 * @version 1.0.7
 */
public class ApiHandlerDefault implements ApiHandler{

    /**
     * Method invoked before handling each API request. This method can be used for pre-processing logic.
     * 
     * @param dw The DataWrapper containing the request body.
     * @param req The HttpServletRequest providing request information.
     * @return DataModel that can be used for further processing or null if no preprocessing is required.
     * 
     * This method is ideal for implementing logic that needs to be executed before the main handling of the request, such as authentication checks, logging, or request modification.
     */
    @Override
    public DataModel beforeHandleRequest(DataWrapper dw, HttpServletRequest req) {return null;}

    /**
     * Method called after beforeHandleRequest and before the main request processing. 
     * This method can be used for authority checks, such as verifying user permissions or roles.
     * 
     * @param dw The DataWrapper containing the request body, potentially modified by previous processing.
     * @param req The HttpServletRequest providing request information.
     * @return DataModel that can be used for further processing or null if no specific authority handling is needed.
     *
     * This method is ideal for implementing authorization logic, ensuring that the incoming request complies with the necessary security constraints before proceeding to the main processing logic.
     */
    @Override
    public DataModel handleAuthority(DataWrapper dw, HttpServletRequest req) {return null;}

    /**
     * Method called after handleAuthority and before the main request processing. 
     * This method is intended for logging purposes, allowing developers to record details about the request.
     * 
     * @param dw The DataWrapper containing the request body, potentially modified by previous processing.
     * @param req The HttpServletRequest providing request information.
     *
     * This method is typically used for logging request details, such as parameters, headers, or other relevant information. It is part of the request handling sequence and provides a centralized place to implement logging logic across all API requests.
     */
    @Override
    public void handleLog(DataWrapper dw, HttpServletRequest req) {}

    /**
     * Method called when an ApiException occurs during the processing of a request.
     * This method is responsible for handling the exception and sending the response back to the client.
     * 
     * @param e The ApiException that was raised during request processing.
     * @param dw The DataWrapper containing the request body, which may be modified to include error details.
     * @param req The HttpServletRequest providing request information.
     * @return ResponseEntity containing the DataWrapper with error details and the appropriate HTTP status.
     * 
     * ApiException is typically thrown from within the ApiController and represents system-level exceptions encountered during API operations.
     * This method defines a default behavior where the DataWrapper includes the status as "error," along with the error code and a default error message ("An undefined error occurred. Contact your system administrator").
     * The response is sent back to the client with an HTTP status of INTERNAL_SERVER_ERROR.
     */
    @Override
    public ResponseEntity<DataWrapper> handleApiException(ApiException e, DataWrapper dw, HttpServletRequest req) {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.putString("status", "error");
        dataWrapper.putString("code", e.getCode());
        dataWrapper.putString("message", "An undefined error occurred. Contact your system administrator.");
        
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dataWrapper);
    }

    /**
     * Method called when a ServiceRuntimeException occurs during the processing of a request.
     * This method handles the exception and prepares the response to be sent back to the client.
     * 
     * @param e The ServiceRuntimeException that was raised during service logic execution.
     * @param dw The DataWrapper containing the request body, which may be modified to include error details.
     * @param req The HttpServletRequest providing request information.
     * @return ResponseEntity containing the DataWrapper with error details and the appropriate HTTP status.
     * 
     * ServiceRuntimeException is typically thrown from within the service layer logic and represents custom exceptions encountered during service operations.
     * The default behavior defined in this method includes the status as "error" in the DataWrapper, along with the error code and message from the exception.
     * The response is sent back to the client with an HTTP status of INTERNAL_SERVER_ERROR.
     * 
     * <p>Example of triggering this exception in service layer logic:</p>
     * <pre>
     *     throw new ServiceRuntimeException("This is Runtime exception message test", "ERR0001");
     * </pre>
     */
    @Override
    public ResponseEntity<DataWrapper> handleServiceRuntimeException(ServiceRuntimeException e, DataWrapper dw, HttpServletRequest req) {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.putString("status", "error");
        dataWrapper.putString("code", e.getCode());
        dataWrapper.putString("message", e.getMessage());
        
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dataWrapper);
    }

    /**
     * Method called when an unhandled Exception occurs during the processing of a request.
     * This method is responsible for handling these exceptions and formulating the response back to the client.
     * 
     * @param e The Exception that was raised during request processing.
     * @param dw The DataWrapper containing the request body, which may be modified to include error details.
     * @param req The HttpServletRequest providing request information.
     * @return ResponseEntity containing the DataWrapper with error details and the appropriate HTTP status.
     * 
     * The default implementation sets the status as "error" in the DataWrapper, includes the class name of the exception as the error code, 
     * and sets a default error message ("An undefined error occurred. Contact your system administrator"). 
     * The response is sent back to the client with an HTTP status of INTERNAL_SERVER_ERROR, indicating a server-side problem.
     */
    @Override
    public ResponseEntity<DataWrapper> handleException(Exception e, DataWrapper dw, HttpServletRequest req) {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.putString("status", "error");
        dataWrapper.putString("code", e.getClass().toString());
        dataWrapper.putString("message", "An undefined error occurred. Contact your system administrator.");
        
        e.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dataWrapper);
    }

    /**
     * Method called when a Throwable is encountered during the processing of a request.
     * Similar to handleException, this method deals with Throwable objects, representing errors and exceptions that are more general than Exception.
     * 
     * @param t The Throwable that was raised during request processing.
     * @param dw The DataWrapper containing the request body, which may be modified to include error details.
     * @param req The HttpServletRequest providing request information.
     * @return ResponseEntity containing the DataWrapper with error details and the appropriate HTTP status.
     * 
     * The default behavior is to set the status as "error" in the DataWrapper, include the class name of the Throwable as the error code,
     * and use a default error message ("An undefined error occurred. Contact your system administrator").
     * The response is sent back with an HTTP status of INTERNAL_SERVER_ERROR, indicating a serious problem that needs attention.
     */
    @Override
    public ResponseEntity<DataWrapper> handleThrowable(Throwable t, DataWrapper dw, HttpServletRequest req) {
        DataWrapper dataWrapper = new DataWrapper();
        dataWrapper.putString("status", "error");
        dataWrapper.putString("code", t.getClass().toString());
        dataWrapper.putString("message", "An undefined error occurred. Contact your system administrator.");
        
        t.printStackTrace();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(dataWrapper);
    }

    /**
     * Method called as the last step in the request processing cycle, just before sending the response back to the client.
     * This method provides an opportunity for final modifications or additional processing of the response data.
     * 
     * @param requestDw The DataWrapper containing the original request body.
     * @param responesDw The DataWrapper containing the response data that will be sent back to the client. This can be modified if needed.
     * @param req The HttpServletRequest providing request information.
     * 
     * This method is ideal for implementing logic that needs to be executed after the main handling of the request is complete, such as cleanup operations, auditing, or modifying the response data before it's sent to the client.
     */
    @Override
    public void afterHandleRequest(DataWrapper requestDw, DataWrapper responesDw, HttpServletRequest req) {};
}

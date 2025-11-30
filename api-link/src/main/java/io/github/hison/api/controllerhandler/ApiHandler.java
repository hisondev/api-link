package io.github.hison.api.controllerhandler;

import org.springframework.http.ResponseEntity;

import io.github.hison.api.exception.ApiException;
import io.github.hison.api.exception.ServiceRuntimeException;
import io.github.hison.data.model.DataModel;
import io.github.hison.data.wrapper.DataWrapper;
import jakarta.servlet.http.HttpServletRequest;

/** 
 * @author Hani son
 * @version 1.0.7
 */
public interface ApiHandler {
    DataModel beforeHandleRequest(DataWrapper dw, HttpServletRequest req);

    DataModel handleAuthority(DataWrapper dw, HttpServletRequest req);

    void handleLog(DataWrapper dw, HttpServletRequest req);

    ResponseEntity<DataWrapper> handleApiException(ApiException e, DataWrapper dw, HttpServletRequest req);

    ResponseEntity<DataWrapper> handleServiceRuntimeException(ServiceRuntimeException e, DataWrapper dw, HttpServletRequest req);

    ResponseEntity<DataWrapper> handleException(Exception e, DataWrapper dw, HttpServletRequest req);

    ResponseEntity<DataWrapper> handleThrowable(Throwable t, DataWrapper dw, HttpServletRequest req) ;

    void afterHandleRequest(DataWrapper requestDw, DataWrapper responesDw, HttpServletRequest req);
}

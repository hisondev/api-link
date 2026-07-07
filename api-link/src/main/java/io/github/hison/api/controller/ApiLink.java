package io.github.hison.api.controller;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.github.hison.api.controllerhandler.ApiHandler;
import io.github.hison.api.controllerhandler.ApiHandlerFactory;
import io.github.hison.api.exception.ApiException;
import io.github.hison.api.exception.ServiceRuntimeException;
import io.github.hison.api.util.ApiLinkService;
import io.github.hison.api.util.HisonService;
import io.github.hison.api.util.MethodHandleUtil;
import io.github.hison.data.model.DataModel;
import io.github.hison.data.wrapper.DataWrapper;
import jakarta.servlet.http.HttpServletRequest;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * 
 * ApiController is a controller class that handles HTTP POST, PUT, PATCH, and DELETE methods.
 * This controller facilitates the development process by allowing business logic to be called using the 'cmd' parameter, eliminating the need for creating separate controllers.
 * 
 * <p>Key Features:</p>
 * <ul>
 * <li>Customizable ApiController behavior through ApiHandler.</li>
 * <li>Convenient exception handling using ApiException and ServiceRuntimeException.</li>
 * <li>Developer customization of WebSocket connections and caching functionalities through CachingWebSocketHandler and CachingHandler.</li>
 * </ul>
 * <p>Usage Example:</p>
 * <pre>
 *    ApiController controller = new ApiController();
 *    ResponseEntity&lt;DataWrapper&gt; response = controller.handlePost(dataWrapper, request);
 * </pre>
 * 
 * @author Hani son
 * @version 2.0.1
 */
public class ApiLink {
    private final ApplicationContext applicationContext;

    private ApiHandler handler;

    private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

    /**
     * Constructor injection of the Spring {@link ApplicationContext} (used to look up service beans
     * and the {@link ApiHandler}). Subclasses (e.g. a custom controller extending {@code ApiLink})
     * must pass the context via {@code super(applicationContext)}.
     *
     * @param applicationContext the Spring application context
     */
    protected ApiLink(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Resolves the {@link ApiHandler} lazily, on first request, so it is independent of bean
     * creation order (no {@code @DependsOn} needed).
     *
     * <p>Resolution priority:</p>
     * <ol>
     *   <li>A custom {@code ApiHandler} registered as a Spring bean (recommended).</li>
     *   <li>{@link ApiHandlerFactory#getHandler()} — supports the legacy {@code setCustomHandler()} style.</li>
     *   <li>The default handler (via the factory).</li>
     * </ol>
     * Because this runs at the first HTTP request (after the context is fully initialized),
     * a custom handler bean is reliably picked up regardless of when it was created.
     */
    private ApiHandler handler() {
        ApiHandler h = this.handler;
        if (h == null) {
            if (applicationContext != null) {
                h = applicationContext.getBeanProvider(ApiHandler.class).getIfAvailable(ApiHandlerFactory::getHandler);
            } else {
                h = ApiHandlerFactory.getHandler();
            }
            this.handler = h;
        }
        return h;
    }

    @GetMapping
    public ResponseEntity<DataWrapper> handleGet(HttpServletRequest req) {
        DataWrapper dw = new DataWrapper();
        req.getParameterMap().forEach((k, v) -> {
            if (v != null && v.length > 0) {
                dw.putString(k, v[0]);
            } else {
                dw.putString(k, "");
            }
        });
        return respones(dw, req);
    }

    @PostMapping
    public ResponseEntity<DataWrapper> handlePost(@RequestBody(required = false) DataWrapper dw, HttpServletRequest req) {
        return respones(dw, req);
    }

    @PutMapping
    public ResponseEntity<DataWrapper> handlePut(@RequestBody(required = false) DataWrapper dw, HttpServletRequest req) {
        return respones(dw, req);
    }

    @PatchMapping
    public ResponseEntity<DataWrapper> handlePatch(@RequestBody(required = false) DataWrapper dw, HttpServletRequest req) {
        return respones(dw, req);
    }

    @DeleteMapping
    public ResponseEntity<DataWrapper> handleDelete(@RequestBody(required = false) DataWrapper dw, HttpServletRequest req) {
        return respones(dw, req);
    }

    private ResponseEntity<DataWrapper> respones(@RequestBody DataWrapper dw, HttpServletRequest req) {
        ApiHandler handler = handler();
        DataWrapper requestDw = (dw != null ? dw : new DataWrapper());

        try {
            ResponseEntity<DataWrapper> responseEntity = ResponseEntity.noContent().build();

            requestDw.putDataModel("resultBeforeHandleRequest", handler.beforeHandleRequest(requestDw, req));
            responseEntity = handleRequest(requestDw, req);

            DataWrapper responseDw = null;
            if (responseEntity != null) {
                responseDw = responseEntity.getBody();
            }

            // The handler always receives a non-null DataWrapper for convenience,
            // but the actual client response preserves null when the service returned null.
            DataWrapper responseDwForHandler = (responseDw != null) ? responseDw : new DataWrapper();
            handler.afterHandleRequest(requestDw, responseDwForHandler, req);

            if (responseEntity == null) {
                return ResponseEntity.noContent().build();
            }
            if (responseEntity.getStatusCode().value() == 204) {
                return responseEntity;
            }

            // Preserve a null body (service intentionally returned null) instead of forcing an empty DataWrapper.
            return ResponseEntity
                    .status(responseEntity.getStatusCode())
                    .headers(responseEntity.getHeaders())
                    .body(responseDw);

        } catch (ApiException e) {
            return handler.handleApiException(e, requestDw, req);
        } catch (ServiceRuntimeException e) {
            return handler.handleServiceRuntimeException(e, requestDw, req);
        } catch (Exception e) {
            return handler.handleException(e, requestDw, req);
        } catch (Throwable t) {
            return handler.handleThrowable(t, requestDw, req);
        }
    }

    private ResponseEntity<DataWrapper> handleRequest(DataWrapper dw, HttpServletRequest req) throws Throwable {
        ApiHandler handler = handler();
        DataModel resultCheckAuthority = handler.handleAuthority(dw, req);
        dw.putDataModel("resultCheckAuthority", resultCheckAuthority);
        handler.handleLog(dw, req);

        if (!dw.containsKey("cmd")) {
            throw new ApiException("There is no cmd", "APIERROR0001");
        }
        String _cmd = (String) dw.getString("cmd");

        Object serviceResult = callService(_cmd, dw, req);

        if (serviceResult == null) {
            return ResponseEntity.noContent().build();
        }

        if (serviceResult instanceof ResponseEntity) {
            ResponseEntity<?> re = (ResponseEntity<?>) serviceResult;
            Object body = re.getBody();
            if (body == null) {
                @SuppressWarnings("unchecked")
                ResponseEntity<DataWrapper> casted = (ResponseEntity<DataWrapper>) re;
                return casted;
            }
            if (body instanceof DataWrapper) {
                @SuppressWarnings("unchecked")
                ResponseEntity<DataWrapper> casted = (ResponseEntity<DataWrapper>) re;
                return casted;
            }
            throw new ApiException("Invalid return type", "APIERROR0008");
        }

        if (serviceResult instanceof DataWrapper) {
            return ResponseEntity.ok().body((DataWrapper) serviceResult);
        }

        throw new ApiException("Invalid return type", "APIERROR0008");
    }

    private Object callService(String cmd, DataWrapper dw, HttpServletRequest req) throws Throwable {
        String[] cmdParts = cmd.split("\\.");
        if (cmdParts.length != 2) {
            throw new ApiException("Invalid cmd format", "APIERROR0002");
        }
        String serviceName = cmdParts[0];
        String methodName = cmdParts[1];

        if (serviceName == null || serviceName.trim().isEmpty()) {
            throw new ApiException("Service name is missing.", "APIERROR0002");
        }
        if (methodName == null || methodName.trim().isEmpty()) {
            throw new ApiException("Method name is missing.", "APIERROR0002");
        }

        String beanName = Objects.requireNonNull(decapitalizeFirstLetter(serviceName));

        Object service;
        try {
            service = applicationContext.getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            throw new ApiException("There is no service: " + serviceName, "APIERROR0003");
        }

        Class<?> beanType = applicationContext.getType(beanName);
        if (!isApiLinkService(beanType != null ? beanType : service.getClass())) {
            throw new ApiException("This is not an api-link service: " + serviceName, "APIERROR0007");
        }

        try {
            MethodHandle targetMethodHandle = MethodHandleUtil.getFlexibleMethodHandle(
                    service.getClass(), methodName, service);

            if (targetMethodHandle.type().parameterCount() == 1) {
                return targetMethodHandle.invokeWithArguments(dw);
            } else if (targetMethodHandle.type().parameterCount() == 0) {
                return targetMethodHandle.invokeWithArguments();
            } else {
                throw new ApiException("Method not found: " + methodName, "APIERROR0004");
            }

        } catch (NoSuchMethodException e) {
            try {
                return invokeByReflection(service, methodName, dw, req);
            } catch (NoSuchMethodException ex) {
                throw new ApiException("no such method: " + methodName, "APIERROR0005");
            } catch (IllegalAccessException ex) {
                throw new ApiException("This is illegal access: " + cmd, "APIERROR0006");
            }
        } catch (IllegalAccessException e) {
            throw new ApiException("This is illegal access: " + cmd, "APIERROR0006");
        }
    }

    private Object invokeByReflection(Object service, String methodName, DataWrapper dw, HttpServletRequest req)
            throws Throwable, NoSuchMethodException, IllegalAccessException {

        Method[] methods = service.getClass().getMethods();

        for (Method method : methods) {
            if (!method.getName().equals(methodName)) {
                continue;
            }

            Class<?>[] paramTypes = method.getParameterTypes();
            Object[] args = new Object[paramTypes.length];

            if (paramTypes.length == 0) {
                return invokeMethod(service, method, args);
            }

            String[] paramNames = parameterNameDiscoverer.getParameterNames(method);
            if (paramNames == null) {
                throw new ApiException("Parameter names are not available. Compile with -parameters.", "APIERROR0009");
            }

            boolean matched = true;

            for (int i = 0; i < paramTypes.length; i++) {
                Class<?> paramType = paramTypes[i];

                if (DataWrapper.class.isAssignableFrom(paramType)) {
                    args[i] = dw;
                    continue;
                }
                if (HttpServletRequest.class.isAssignableFrom(paramType)) {
                    args[i] = req;
                    continue;
                }

                if (!String.class.equals(paramType)) {
                    matched = false;
                    break;
                }

                String key = paramNames[i];
                if (!dw.containsKey(key)) {
                    matched = false;
                    break;
                }
                args[i] = dw.getString(key);
            }

            if (!matched) {
                continue;
            }

            return invokeMethod(service, method, args);
        }

        throw new NoSuchMethodException("Method not found: " + methodName);
    }

    private static Object invokeMethod(Object service, Method method, Object[] args) throws Throwable, IllegalAccessException {
        try {
            return method.invoke(service, args);
        } catch (IllegalAccessException e) {
            throw e;
        } catch (InvocationTargetException e) {
            if (e.getCause() != null) {
                throw e.getCause();
            }
            throw e;
        }
    }

    /**
     * Returns whether the given class is exposed to api-link.
     * Recognizes {@link ApiLinkService} (preferred) as well as the deprecated {@link HisonService} (backward compatibility).
     */
    private static boolean isApiLinkService(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }
        if (isMarked(clazz)) {
            return true;
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass != null && isMarked(superClass)) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("deprecation")
    private static boolean isMarked(Class<?> clazz) {
        return clazz.isAnnotationPresent(ApiLinkService.class) || clazz.isAnnotationPresent(HisonService.class);
    }

    private static String decapitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char[] chars = str.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
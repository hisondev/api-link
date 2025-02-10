package io.github.hison.api.caching;

import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;

/** 
 * @author Hani son
 * @version 1.0.1
 */
public class WebSocketConfig implements WebSocketConfigurer {
    private final CachingWebSocketSessionManager sessionManager = CachingWebSocketSessionManager.getInstance();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(sessionManager), sessionManager.getEndPoint());
    }
}

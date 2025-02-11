package io.github.hison.api.caching;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/** 
 * @author Hani son
 * @version 1.0.2
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final CachingWebSocketSessionManager sessionManager = CachingWebSocketSessionManager.getInstance();
    private final CachingHandler handler;

    public WebSocketConfig() {
        this.handler = CachingHandlerFactory.getHandler();
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        handler.setRegistry(registry.addHandler(new WebSocketHandler(sessionManager), sessionManager.getEndPoint()));
    }
}

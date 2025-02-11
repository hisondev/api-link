package io.github.hison.api.caching;

import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import io.github.hison.api.handler.CorsValidator;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

/** 
 * @author Hani son
 * @version 1.0.3
 */
@Configuration
@EnableWebSocket
@ConditionalOnMissingBean(WebSocketConfigurer.class)
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${hison.link.websocket.endpoint:/hison-websocket-endpoint}")
    private String websocketEndpoints;

    @Value("${hison.link.api.cors.origins:*}")
    private String corsOrigins;

    @Value("${hison.link.api.cors.allow-credentials:false}")
    private boolean allowCredentials;

    private final CorsValidator corsValidator;
    private final CachingWebSocketSessionManager sessionManager = CachingWebSocketSessionManager.getInstance();

    public WebSocketConfig(CorsValidator corsValidator) {
        this.corsValidator = corsValidator;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        List<String> endpoints = Arrays.asList(websocketEndpoints.split(","));
        List<String> origins = corsValidator.parseOrigins(corsOrigins);
        corsValidator.validateCorsSettings(origins, allowCredentials);

        for (String endpoint : endpoints) {
            registry.addHandler(new WebSocketHandler(sessionManager), endpoint.trim())
                    .setAllowedOrigins(origins.toArray(new String[0]));
        }
    }
}
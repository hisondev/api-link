package io.github.hison.api.cachinghandler;

import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import io.github.hison.api.caching.CachingWebSocketSessionManager;

import org.springframework.web.socket.CloseStatus;

/**
 * @author Hani son
 * @version 2.0.2
 */
public class CachingWebSocketHandler extends AbstractWebSocketHandler {
    private final CachingWebSocketSessionManager sessionManager;

    public CachingWebSocketHandler(CachingWebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        sessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessionManager.removeSession(session);
    }
}

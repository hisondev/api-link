package io.github.hison.api.caching;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

/** 
 * @author Hani son
 * @version 1.0.0
 */
public class WebSocketHandler extends AbstractWebSocketHandler {
    private final CachingWebSocketSessionManager sessionManager;

    public WebSocketHandler(CachingWebSocketSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessionManager.addSession(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessionManager.removeSession(session);
    }
}

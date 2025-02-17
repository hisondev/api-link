package io.github.hison.api.caching;

import org.springframework.web.socket.WebSocketSession;

import io.github.hison.api.cachinghandler.CachingHandler;
import io.github.hison.api.cachinghandler.CachingHandlerFactory;

import java.util.concurrent.CopyOnWriteArrayList;

/** 
 * @author Hani son
 * @version 1.0.7
 */
public class CachingWebSocketSessionManager {
    private static CachingWebSocketSessionManager instance;
    private final CopyOnWriteArrayList<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    private final CachingHandler handler;
    private CachingWebSocketSessionManager() {
        this.handler = CachingHandlerFactory.getHandler();
    }

    public static synchronized CachingWebSocketSessionManager getInstance() {
        if (instance == null) {
            instance = new CachingWebSocketSessionManager();
        }
        return instance;
    }

    public void addSession(WebSocketSession session) {
        handler.addSession(sessions, session);
    }
    
    public void removeSession(WebSocketSession session) {
        handler.removeSession(sessions, session);
    }

    public void notifyAllSessions(String message) {
        handler.notifyAllSessions(sessions, message);
    }
}

package io.github.hison.api.caching;

import org.springframework.web.socket.WebSocketSession;
import java.util.concurrent.CopyOnWriteArrayList;

/** 
 * @author Hani son
 * @version 1.0.2
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

    public String getEndPoint() {
        return handler.getEndPoint();
    }
}

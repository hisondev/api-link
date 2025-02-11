package io.github.hison.api.caching;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistration;

/** 
 * @author Hani son
 * @version 1.0.2
 */
public interface CachingHandler {
    public void setRegistry(WebSocketHandlerRegistration  registry);

    public void addSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session);
    
    public void removeSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session);

    public void notifyAllSessions(CopyOnWriteArrayList<WebSocketSession> sessions, String message);

    public String getEndPoint();
}

package io.github.hison.api.caching;

import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.web.socket.WebSocketSession;

/** 
 * @author Hani son
 * @version 1.0.4
 */
public interface CachingHandler {

    public void addSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session);
    
    public void removeSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session);

    public void notifyAllSessions(CopyOnWriteArrayList<WebSocketSession> sessions, String message);
}

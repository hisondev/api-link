package io.github.hison.api.cachinghandler;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

/**
 * Default implementation of CachingHandler for WebSocket-based caching logic.
 * Manages WebSocket sessions and facilitates sending messages to all connected sessions.
 * 
 * <p>Key Methods:</p>
 * <ul>
 * <li>{@code addSession}: Adds a new WebSocket session to the session list.</li>
 * <li>{@code removeSession}: Removes a WebSocket session from the session list.</li>
 * <li>{@code notifyAllSessions}: Sends a message to all open WebSocket sessions.</li>
 * <li>{@code getEndPoint}: Returns the WebSocket endpoint identifier.</li>
 * </ul>
 * <p>Messaging Clients:</p>
 * To send WebSocket messages to clients, service logic can use the CachingWebSocketSessionManager. For instance, in a MemberService:
 * <pre>
 *     &#64;Service
 *     public class MemberService {
 *         private final CachingWebSocketSessionManager cachingWebSocketSessionManager = CachingWebSocketSessionManager.getInstance();
 *
 *         public DataWrapper createMember(DataWrapper dw) {
 *             cachingWebSocketSessionManager.notifyAllSessions("updated");
 *             // Additional logic...
 *         }
 *     }
 * </pre>
 * <p>Customization:</p>
 * Developers can create custom caching handlers, like CustomCachingHandler, implementing CachingHandler. Register this custom handler at application startup:
 * <pre>
 *     &#64;SpringBootApplication
 *     public class DemoApplication {
 *         public static void main(String[] args) {
 *             CustomCachingHandler.register();
 *         }
 *     }
 * </pre>
 * CachingHandlerFactory allows for the use of either custom or default caching handlers.
 * This class is pivotal for managing WebSocket connections in a caching context and provides a foundation for WebSocket communication and session management.
 * 
 * @author Hani son
 * @version 2.0.0
 */
public class CachingHandlerDefault implements CachingHandler{
    /**
     * Adds a WebSocket session to the provided CopyOnWriteArrayList of WebSocketSession objects.
     * This method is used to maintain a list of active WebSocket sessions.
     *
     * @param sessions The CopyOnWriteArrayList of WebSocketSession objects representing the current active sessions.
     * @param session The WebSocketSession to be added to the sessions list.
     *
     * Usage of this method ensures that the session list is updated with new WebSocket connections, allowing for subsequent communication with these sessions.
     */
    @Override
    public void addSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session) {
        sessions.add(session);
    }
    
    /**
     * Removes a WebSocket session from the provided CopyOnWriteArrayList of WebSocketSession objects.
     * This method is utilized to manage and update the list of active WebSocket sessions by removing sessions that are no longer active or needed.
     *
     * @param sessions The CopyOnWriteArrayList of WebSocketSession objects representing the current active sessions.
     * @param session The WebSocketSession to be removed from the sessions list.
     *
     * The use of this method is crucial for maintaining an accurate list of active sessions, ensuring that communications are only attempted with sessions that are still open and active.
     */
    @Override
    public void removeSession(CopyOnWriteArrayList<WebSocketSession> sessions, WebSocketSession session) {
        sessions.remove(session);
    }

    /**
     * Sends the specified message to all WebSocket sessions in the provided CopyOnWriteArrayList of WebSocketSession objects.
     * This method is used for broadcasting messages to all connected WebSocket clients.
     *
     * @param sessions The CopyOnWriteArrayList of WebSocketSession objects representing the current active sessions.
     * @param message The String message to be sent to all active sessions.
     *
     * This method iterates through each session in the list, checks if the session is open, and then sends the message.
     * It is an essential method for real-time communication with multiple clients connected via WebSocket.
     *
     * Note: This method handles IOExceptions that may occur during message sending by printing the stack trace, but more sophisticated error handling may be implemented as needed.
     */
    @Override
    public void notifyAllSessions(CopyOnWriteArrayList<WebSocketSession> sessions, String message) {
        for (WebSocketSession session : sessions) {
            try {
                if (session.isOpen()) {
                    if(message == null) {
                        message = "";
                    }
                    session.sendMessage(new TextMessage(message));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

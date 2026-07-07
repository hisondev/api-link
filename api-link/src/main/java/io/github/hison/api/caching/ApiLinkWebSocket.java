package io.github.hison.api.caching;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/**
 * Auto-registered WebSocket configuration for the api-link <b>cache-invalidation signal channel</b>.
 *
 * <p>This is <b>not</b> a chat/messaging server. Its sole purpose is to let the server broadcast a
 * short signal (see {@link CachingWebSocketSessionManager#notifyAllSessions(String)}) so that the
 * client-side caching module (hisonjs {@code CachingModule}) can invalidate/refresh cached api-link
 * responses. Targeted delivery, rooms, per-user identity and inbound messages are intentionally out
 * of scope — build a dedicated WebSocket layer for those.</p>
 *
 * <p>Registration:</p>
 * <ul>
 *   <li>Auto-registered on Boot 3 (via {@code AutoConfiguration.imports}).</li>
 *   <li>Disabled when {@code hison.link.websocket.enabled=false}.</li>
 *   <li>Skipped if the application already defines its own {@link CachingWebSocket} bean
 *       ({@link ConditionalOnMissingBean}), allowing full override.</li>
 * </ul>
 *
 * @author Hani Son
 * @version 2.0.2
 */
@Configuration
@EnableWebSocket
@ConditionalOnProperty(name = "hison.link.websocket.enabled", havingValue = "true", matchIfMissing = true)
@ConditionalOnMissingBean(CachingWebSocket.class)
public class ApiLinkWebSocket extends CachingWebSocket{}

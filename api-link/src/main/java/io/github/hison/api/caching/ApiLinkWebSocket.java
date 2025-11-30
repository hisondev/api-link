package io.github.hison.api.caching;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/** 
 * WebSocket configuration with CORS validation using static methods.
 * 
 * @author Hani Son
 * @version 2.0.0
 */
@Configuration
@EnableWebSocket
@ConditionalOnMissingBean(CachingWebSocket.class)
public class ApiLinkWebSocket extends CachingWebSocket{}

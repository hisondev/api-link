package io.github.hison.api.caching;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

/** 
 * WebSocket configuration with CORS validation using static methods.
 * 
 * @author Hani Son
 * @version 1.0.5
 */
@Configuration
@EnableWebSocket
@ConditionalOnMissingBean(WebSocketConfigurerImpl.class)
public class WebSocketConfig extends WebSocketConfigurerImpl{}

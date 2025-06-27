package com.rva.dungeon.config;

import com.rva.dungeon.websocket.ConsoleWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ConsoleWebSocketHandler consoleHandler;

    public WebSocketConfig(ConsoleWebSocketHandler consoleHandler) {
        this.consoleHandler = consoleHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(consoleHandler, "/console").setAllowedOrigins("*");
    }

}

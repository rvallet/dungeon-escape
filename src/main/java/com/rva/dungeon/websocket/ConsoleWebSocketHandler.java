package com.rva.dungeon.websocket;

import com.rva.dungeon.service.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class ConsoleWebSocketHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleWebSocketHandler.class);

    private final GameService gameService;

    private static final ConcurrentHashMap<WebSocketSession, Boolean> sessions = new ConcurrentHashMap<>();

    public ConsoleWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session, true);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String command = message.getPayload();
        // Log the command received from the WebSocket client
        logger.info("Received command from WebSocket: {}", command);
        // Process the command using the GameService
        gameService.processUserInput(command);
    }

    public static void broadcast(String message) {
        sessions.keySet().forEach(s -> {
            try {
                if (s.isOpen()) s.sendMessage(new TextMessage(message));
            } catch (Exception e) {
                logger.error("Error sending message to WebSocket session: {}", e.getMessage());
            }
        });
    }

}

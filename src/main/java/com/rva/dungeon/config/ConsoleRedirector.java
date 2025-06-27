package com.rva.dungeon.config;

import com.rva.dungeon.websocket.ConsoleWebSocketHandler;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.io.OutputStream;
import java.io.PrintStream;

@Component
public class ConsoleRedirector {

    private final ConsoleWebSocketHandler websocketHandler;

    public ConsoleRedirector(ConsoleWebSocketHandler websocketHandler) {
        this.websocketHandler = websocketHandler;
    }

    @PostConstruct
    public void redirect() {
        OutputStream out = new OutputStream() {
            StringBuilder buffer = new StringBuilder();

            @Override
            public void write(int b) {
                buffer.append((char) b);
                if (b == '\n') {
                    // Envoyer la ligne à tous les clients WebSocket
                    ConsoleWebSocketHandler.broadcast(buffer.toString());
                    buffer.setLength(0);
                }
            }
        };
        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }
}

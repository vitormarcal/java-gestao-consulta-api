package br.com.sismedicina.gestor.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SocketIOConfig implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);
    private final Map<String, List<UUID>> salas = Collections.synchronizedMap(new HashMap<>());
    private final SocketIOServer server;

    public SocketIOConfig() {
        Configuration config = new Configuration();
        config.setPort(3000);

        server = new SocketIOServer(config);
    }

    @PreDestroy
    public void destroy() {
        server.stop();
    }

    @Override
    public void run(String... args) throws Exception {
        server.addConnectListener(client -> {
            logger.info("{} conectou", client.getSessionId());
            logger.info("Sala {}", salas);
        });

        server.addEventListener("message", String.class, (socket, data, ackRequest) -> {
            ObjectNode ob = new ObjectMapper().readValue(data, ObjectNode.class);
            String room = ob.get("room").asText();
            String dataText = ob.get("data").toString();

            if (!salas.containsKey(room)) {
                salas.put(room, new ArrayList<>());
                salas.get(room).add(socket.getSessionId());
            } else if (salas.get(room).size() < 2) {
                if (salas.get(room).stream().noneMatch(i -> i.equals(socket.getSessionId()))) {
                    salas.get(room).add(socket.getSessionId());
                }
            }

            if (salas.get(room).size() == 2) {

                salas.get(room).forEach(s -> {
                    if (!s.equals(socket.getSessionId()) && server.getClient(s) != null && salas.get(room).size() <= 2) {
                        socket.sendEvent("message", dataText);
                    }
                });

            }


        });


        server.addEventListener("join", String.class, (socket, roomId, ackRequest) -> {
            logger.info("join {}", roomId);
            if (salas.containsKey(roomId) && salas.get(roomId).size() == 2) {
                socket.sendEvent("reject", new Error("Room is full"));
            }
        });


        server.addEventListener("leave", String.class, (socket, roomId, ackRequest) -> {
            logger.info("leave {}", roomId);
            sairDaSala(socket, roomId);
        });


        server.addEventListener("disconnect'", String.class, (socket, roomId, ackRequest) -> {
            logger.info(socket.getSessionId() + "has been disconnected");
            sairDaSala(socket, roomId);
        });


        server.start();
    }

    private void sairDaSala(SocketIOClient socket, String roomId) {
        List<UUID> list = salas.getOrDefault(roomId, Collections.emptyList())
                .stream()
                .filter(s -> !s.equals(socket.getSessionId()))
                .collect(Collectors.toList());
        if (list.isEmpty()) {
            salas.remove(roomId);
        } else {
            salas.put(roomId, list);
        }

        logger.info("Salas {}", salas);
    }


    public static class Error {
        private final String error;


        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

    }


}


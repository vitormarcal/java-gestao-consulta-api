package br.com.sismedicina.gestor.config;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class SocketIOConfig {

    private static final Logger logger = LoggerFactory.getLogger(SocketIOConfig.class);
    private final Map<String, List<UUID>> salas = new HashMap<>();
    private final SocketIOServer socket;

    public SocketIOConfig() {
        Configuration config = new Configuration();
        config.setPort(3000);

        socket = new SocketIOServer(config);
    }

    @PreDestroy
    public void destroy() {
        socket.stop();
    }

    @PostConstruct
    public void init() {


        socket.addConnectListener(client -> {
            logger.info("{} conectou", client.getSessionId());
            logger.info("Sala {}", salas);
        });

        socket.addEventListener("message", String.class, (client, data, ackRequest) -> {
            ObjectNode ob = new ObjectMapper().readValue(data, ObjectNode.class);
            String room = ob.get("room").asText();
            String dataText = ob.get("data").toString();

            if (!salas.containsKey(room)) {
                salas.put(room, new ArrayList<>());
                salas.get(room).add(client.getSessionId());
            } else if (salas.get(room).size() < 2) {
                if (salas.get(room).stream().noneMatch(i -> i.equals(client.getSessionId()))) {
                    salas.get(room).add(client.getSessionId());
                }
            }

            if (salas.get(room).size() == 2) {

                salas.get(room).forEach(s -> {
                    if (!s.equals(client.getSessionId()) && socket.getClient(s) != null && salas.get(room).size() <= 2) {
                        socket.getBroadcastOperations().sendEvent("message", dataText);
                    }
                });

            }


        });


        socket.addEventListener("join", String.class, (socketIOClient, roomId, ackRequest) -> {
            logger.info("join {}", roomId);
            if (salas.containsKey(roomId) && salas.get(roomId).size() == 2) {
                socket.getBroadcastOperations().sendEvent("reject", new Error("Room is full"));
            }
        });


        socket.addEventListener("leave", String.class, (client, roomId, ackRequest) -> {
            logger.info("leave {}", roomId);
            sairDaSala(client, roomId);
        });


        socket.addEventListener("disconnect'", String.class, (client, roomId, ackRequest) -> {
            logger.info(client.getSessionId() + "has been disconnected");
            sairDaSala(client, roomId);
        });


        socket.start();
    }

    private void sairDaSala(SocketIOClient client, String roomId) {
        List<UUID> list = salas.getOrDefault(roomId, Collections.emptyList())
                .stream()
                .filter(s -> s.equals(client.getSessionId()))
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


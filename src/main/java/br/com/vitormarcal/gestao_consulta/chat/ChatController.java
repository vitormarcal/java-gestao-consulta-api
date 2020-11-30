package br.com.vitormarcal.gestao_consulta.chat;

import br.com.vitormarcal.gestao_consulta.chat.request.MessageRequest;
import br.com.vitormarcal.gestao_consulta.chat.response.OutputMessageResponse;
import br.com.vitormarcal.gestao_consulta.security.services.UserDetailsImpl;
import br.com.vitormarcal.gestao_consulta.consulta.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.SimpleDateFormat;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ConsultaService consultaService;

    @MessageMapping("/secured/room")
    public void sendSpecific(
            @Payload MessageRequest msg,
            UsernamePasswordAuthenticationToken userDetails,
            @Header("simpSessionId") String sessionId) throws Exception {


        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        chatService.guardarMensagemDaConsulta(msg, sessionId, principal);

        if (msg.getExited()) {
            consultaService.finalizarConsulta(msg.getIdConsulta(), principal);
        }

        OutputMessageResponse out = new OutputMessageResponse(
                msg.getDe(),
                msg.getTexto(),
                new SimpleDateFormat("HH:mm").format(new Date()), msg.getExited());
        simpMessagingTemplate.convertAndSendToUser(
                msg.getPara(), "/secured/room/queue", out);
    }


}

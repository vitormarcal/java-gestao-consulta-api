package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.model.ERole;
import br.com.sismedicina.gestor.model.Mensagem;
import br.com.sismedicina.gestor.model.User;
import br.com.sismedicina.gestor.payload.request.MessageRequest;
import br.com.sismedicina.gestor.repositorios.ConsultaRepositorio;
import br.com.sismedicina.gestor.repositorios.MensagemRepositorio;
import br.com.sismedicina.gestor.repositorios.UserRepositorio;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ChatService {

    @Autowired
    private UserRepositorio userRepositorio;
    @Autowired
    private ConsultaRepositorio consultaRepositorio;
    @Autowired
    private MensagemRepositorio mensagemRepositorio;


    @Transactional
    public void guardarMensagemDaConsulta(MessageRequest msg, String sessionId, UserDetailsImpl principal) {
        if (msg.getDe().equalsIgnoreCase(principal.getUsername())) {

            MembrosChat membrosChat = identificarMembrosDoChat(principal, msg.getPara());

            Optional<Consulta> consulta = consultaRepositorio
                    .findFirstByUserIdAndTecnicoIdAndAndFimHorarioIsNull(membrosChat.userId, membrosChat.tecnicoId);

            if (consulta.isPresent()) {
                Mensagem mensagem = new Mensagem();
                mensagem.setConsultaId(consulta.get().getId());
                mensagem.setEm(LocalDateTime.now());
                mensagem.setDe(msg.getDe());
                mensagem.setPara(msg.getPara());
                mensagem.setEm(LocalDateTime.now());
                mensagem.setSessionId(sessionId);
                mensagem.setTexto(msg.getTexto());
                mensagemRepositorio.save(mensagem);
            }
        }
    }

    private MembrosChat identificarMembrosDoChat(UserDetailsImpl principal, String para) {
        Optional<User> byId = userRepositorio.findById(principal.getId());

        MembrosChat membrosChat = new MembrosChat();
        if (byId.isPresent()) {
            User user1 = byId.get();
            if (user1.getRoles().stream().anyMatch(i -> i.getName().equals(ERole.ROLE_TECNICO))) {
                membrosChat.tecnicoId = user1.getId();
            } else {
                membrosChat.userId = user1.getId();
            }

            byId = userRepositorio.findByUsername(para);

            if (byId.isPresent()) {
                user1 = byId.get();
                if (user1.getRoles().stream().anyMatch(i -> i.getName().equals(ERole.ROLE_TECNICO))) {
                    membrosChat.tecnicoId = user1.getId();
                } else {
                    membrosChat.userId = user1.getId();
                }
            }
        }
        return membrosChat;
    }

    static class MembrosChat {
        Long userId;
        Long tecnicoId;
    }


}

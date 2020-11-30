package br.com.vitormarcal.gestao_consulta.chat;

import br.com.vitormarcal.gestao_consulta.chat.repositorio.MensagemRepositorio;
import br.com.vitormarcal.gestao_consulta.chat.request.MessageRequest;
import br.com.vitormarcal.gestao_consulta.chat.response.OutputMessageResponse;
import br.com.vitormarcal.gestao_consulta.chat.model.Mensagem;
import br.com.vitormarcal.gestao_consulta.consulta.model.Consulta;
import br.com.vitormarcal.gestao_consulta.auth.model.ERole;
import br.com.vitormarcal.gestao_consulta.auth.model.User;
import br.com.vitormarcal.gestao_consulta.consulta.repositorio.ConsultaRepositorio;
import br.com.vitormarcal.gestao_consulta.auth.repositorio.UserRepositorio;
import br.com.vitormarcal.gestao_consulta.security.services.UserDetailsImpl;
import br.com.vitormarcal.gestao_consulta.tecnico.model.Tecnico;
import br.com.vitormarcal.gestao_consulta.tecnico.repositorio.TecnicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private UserRepositorio userRepositorio;
    @Autowired
    private ConsultaRepositorio consultaRepositorio;
    @Autowired
    private MensagemRepositorio mensagemRepositorio;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;


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
                membrosChat.tecnicoId = tecnicoRepositorio.findByUserId(user1.getId()).map(Tecnico::getId).orElse(0L);
            } else {
                membrosChat.userId = user1.getId();
            }

            byId = userRepositorio.findByUsername(para);

            if (byId.isPresent()) {
                user1 = byId.get();
                if (user1.getRoles().stream().anyMatch(i -> i.getName().equals(ERole.ROLE_TECNICO))) {
                    membrosChat.tecnicoId = tecnicoRepositorio.findByUserId(user1.getId()).map(Tecnico::getId).orElse(0L);
                } else {
                    membrosChat.userId = user1.getId();
                }
            }
        }
        return membrosChat;
    }

    public List<OutputMessageResponse> buscarMensagensPorIdConsulta(Long idConsulta, UserDetailsImpl principal) {
        return mensagemRepositorio.findByConsultaAndUsername(idConsulta, principal.getUsername())
                .stream()
                .map(OutputMessageResponse::new)
                .collect(Collectors.toList());
    }

    static class MembrosChat {
        Long userId;
        Long tecnicoId;
    }


}

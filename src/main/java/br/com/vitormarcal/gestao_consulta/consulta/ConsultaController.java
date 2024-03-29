package br.com.vitormarcal.gestao_consulta.consulta;

import br.com.vitormarcal.gestao_consulta.chat.response.OutputMessageResponse;
import br.com.vitormarcal.gestao_consulta.consulta.model.Consulta;
import br.com.vitormarcal.gestao_consulta.consulta.request.FiltroConsultaDisponivelRequest;
import br.com.vitormarcal.gestao_consulta.consulta.response.ConsultaDisponivelResponse;
import br.com.vitormarcal.gestao_consulta.consulta.response.ConsultaResponse;
import br.com.vitormarcal.gestao_consulta.security.services.UserDetailsImpl;
import br.com.vitormarcal.gestao_consulta.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;

    @Autowired
    private ChatService chatService;


    @GetMapping("/disponiveis")
    public List<ConsultaDisponivelResponse> buscarConsultasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        return consultaService.buscarAgendasDisponiveis(filtro);
    }

    @GetMapping("/{idConsulta}")
    public Optional<ConsultaResponse> buscarDadosDaConsulta(@PathVariable Long idConsulta) {
        return consultaService.buscarPorId(idConsulta);
    }

    @GetMapping("/usuario-logado")
    public List<ConsultaDisponivelResponse> buscarConsultasDoUsuarioLogado(UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return consultaService.buscarConsultasDoUsuario(principal);
    }


    @PutMapping("/disponiveis/{idConsulta}")
    @PreAuthorize("hasRole('USER')")
    public Optional<Consulta> buscarConsultasDisponiveis(@PathVariable Long idConsulta, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return consultaService.agendarParaEsteUsuario(idConsulta, principal);
    }

    @DeleteMapping("/disponiveis/{idConsulta}")
    @PreAuthorize("hasRole('TECNICO') or hasRole('ADMIN')")
    public void removerConsultaDisponivel(@PathVariable Long idConsulta) {
        consultaService.removerConsultaDisponivel(idConsulta);
    }

    @GetMapping("/{idConsulta}/mensagens")
    public List<OutputMessageResponse> buscarMensagensDaConsulta(@PathVariable Long idConsulta, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return chatService.buscarMensagensPorIdConsulta(idConsulta, principal);
    }

}

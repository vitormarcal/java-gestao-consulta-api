package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.payload.response.ConsultaResponse;
import br.com.sismedicina.gestor.payload.response.OutputMessageResponse;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import br.com.sismedicina.gestor.services.ChatService;
import br.com.sismedicina.gestor.services.ConsultaService;
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
        return consultaService.buscarConsultasDoUsuario(principal.getId());
    }


    @PutMapping("/disponiveis/{idConsulta}")
    @PreAuthorize("hasRole('USER')")
    public Optional<Consulta> buscarConsultasDisponiveis(@PathVariable Long idConsulta, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return consultaService.agendarParaEsteUsuario(idConsulta, principal);
    }

    @GetMapping("/{idConsulta}/mensagens")
    public List<OutputMessageResponse> buscarMensagensDaConsulta(@PathVariable Long idConsulta, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return chatService.buscarMensagensPorIdConsulta(idConsulta, principal);
    }

}

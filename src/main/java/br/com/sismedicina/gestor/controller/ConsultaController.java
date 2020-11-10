package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
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


    @GetMapping("/disponiveis")
    public List<ConsultaDisponivelResponse> buscarConsultasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        return consultaService.buscarAgendasDisponiveis(filtro);
    }

    @PutMapping("/disponiveis/{idConsulta}")
    @PreAuthorize("hasRole('USER')")
    public Optional<Consulta> buscarConsultasDisponiveis(@PathVariable Long idConsulta, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return consultaService.agendarParaEsteUsuario(idConsulta, principal);
    }

}

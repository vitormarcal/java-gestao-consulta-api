package br.com.vitormarcal.atendimento_agendado.tecnico;

import br.com.vitormarcal.atendimento_agendado.security.services.UserDetailsImpl;
import br.com.vitormarcal.atendimento_agendado.tecnico.model.Tecnico;
import br.com.vitormarcal.atendimento_agendado.tecnico.request.TecnicoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tecnicos")
public class TecnicoController {

    private static final Logger logger = LoggerFactory.getLogger(TecnicoController.class);

    @Autowired
    private TecnicoService tecnicoService;


    @GetMapping
    public List<Tecnico> filtrar() {
        return tecnicoService.filtrar();
    }

    @PreAuthorize("hasRole('TECNICO') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> buscarPorId(@PathVariable Long id) {
        return tecnicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('TECNICO')")
    @GetMapping("/usuario-logado")
    public ResponseEntity<Tecnico> buscarPorId(UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return tecnicoService.buscarPorUsuario(principal.getId())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('TECNICO') or hasRole('ADMIN')")
    public Tecnico salvar(@RequestBody TecnicoRequest tecnicoRequest, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        Tecnico tecnico = tecnicoService.salvar(tecnicoRequest, principal);
        logger.info("Tecnico salvo, agenda aberta para {}", principal.getUsername());
        return tecnico;
    }

    @PreAuthorize("hasRole('TECNICO')")
    @PutMapping("/{id}")
    public Tecnico atualizar(@PathVariable Long id, @RequestBody TecnicoRequest tecnicoRequest) {
        return tecnicoService.atualizar(id, tecnicoRequest);
    }

}

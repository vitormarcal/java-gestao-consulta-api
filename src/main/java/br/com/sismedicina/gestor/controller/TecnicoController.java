package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.dto.TecnicoPayload;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import br.com.sismedicina.gestor.services.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("tecnicos")
public class TecnicoController {

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

    @PostMapping
    @PreAuthorize("hasRole('TECNICO') or hasRole('ADMIN')")
    public Tecnico salvar(@RequestBody TecnicoPayload tecnicoPayload, UsernamePasswordAuthenticationToken userDetails) {
        UserDetailsImpl principal = (UserDetailsImpl) userDetails.getPrincipal();
        return tecnicoService.salvar(tecnicoPayload, principal);
    }

    @PreAuthorize("hasRole('TECNICO') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Tecnico atualizar(@PathVariable Long id, @RequestBody TecnicoPayload tecnicoPayload) {
        return tecnicoService.atualizar(id, tecnicoPayload);
    }


}

package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.dto.TecnicoPayload;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.services.TecnicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> buscarPorId(@PathVariable Integer id) {
        return tecnicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Tecnico salvar(@RequestBody TecnicoPayload tecnicoPayload) {
        return tecnicoService.salvar(tecnicoPayload);
    }

    @PutMapping
    public Tecnico atualizar(@RequestBody TecnicoPayload medicoAtualizacao) {
        return tecnicoService.atualizar(medicoAtualizacao);
    }


}

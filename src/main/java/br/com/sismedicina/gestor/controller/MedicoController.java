package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.dto.MedicoCarga;
import br.com.sismedicina.gestor.model.Medico;
import br.com.sismedicina.gestor.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;


    @GetMapping
    public List<Medico> filtrar() {
        return medicoService.filtrar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> buscarPorId(@PathVariable Integer id) {
        return medicoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Medico salvar(@RequestBody MedicoCarga medicoCarga) {
        return medicoService.salvar(medicoCarga);
    }

    @PutMapping
    public Medico atualizar(@RequestBody MedicoCarga medicoAtualizacao) {
        return medicoService.atualizar(medicoAtualizacao);
    }


}

package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.dto.MedicoCriacao;
import br.com.sismedicina.gestor.model.Medico;
import br.com.sismedicina.gestor.services.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public Optional<Medico> buscarPorId(@PathVariable Integer id) {
        return medicoService.buscarPorId(id);
    }

    @PostMapping
    public Medico salvar(@RequestBody MedicoCriacao medicoCriacao) {
        return medicoService.salvar(medicoCriacao);
    }


}

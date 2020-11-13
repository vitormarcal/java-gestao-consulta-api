package br.com.sismedicina.gestor.especialidade;

import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("especialidades")
public class EspecialidadeController {

    @Autowired
    private EspecialidadeService service;


    @GetMapping
    public Iterable<Especialidade> buscarTodas() {
        return this.service.buscarTodas();
    }

}

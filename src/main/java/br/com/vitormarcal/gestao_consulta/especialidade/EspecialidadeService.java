package br.com.vitormarcal.gestao_consulta.especialidade;

import br.com.vitormarcal.gestao_consulta.especialidade.model.Especialidade;
import br.com.vitormarcal.gestao_consulta.especialidade.repositorio.EspecialidadeRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EspecialidadeService {

    @Autowired
    private EspecialidadeRepositorio repositorio;


    public Iterable<Especialidade> buscarTodas() {
        return repositorio.findAll();
    }
}

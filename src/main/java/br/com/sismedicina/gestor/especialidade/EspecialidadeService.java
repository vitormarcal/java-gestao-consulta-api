package br.com.sismedicina.gestor.especialidade;

import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import br.com.sismedicina.gestor.especialidade.repositorio.EspecialidadeRepositorio;
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

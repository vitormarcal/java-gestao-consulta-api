package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.model.Especialidade;
import br.com.sismedicina.gestor.repositorios.EspecialidadeRepositorio;
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

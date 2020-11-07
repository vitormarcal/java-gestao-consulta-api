package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.dto.TecnicoPayload;
import br.com.sismedicina.gestor.model.Especialidade;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.repositorios.EspecialidadeRepositorio;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;
    @Autowired
    private EspecialidadeRepositorio especialidadeRepositorio;


    public List<Tecnico> filtrar() {
        return tecnicoRepositorio.findAll();
    }

    public Tecnico salvar(TecnicoPayload tecnicoPayload) {

        Tecnico tecnico = new Tecnico();
        BeanUtils.copyProperties(tecnicoPayload, tecnico);

        tecnico.setDiasQueAtende(tecnicoPayload.getDiasQueAtende());

        if (!tecnico.saoDatasValidas()) {
            throw new RuntimeException("As datas não estao em um intervalo valido");
        }

        definirEspecialidade(tecnicoPayload, tecnico);

        return tecnicoRepositorio.save(tecnico);
    }

    public Optional<Tecnico> buscarPorId(Integer id) {
        return tecnicoRepositorio.findById(id);
    }

    public Tecnico atualizar(TecnicoPayload tenicoAtualizacao) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepositorio.findById(tenicoAtualizacao.getId());

        if (tecnicoOptional.isPresent()) {
            Tecnico tecnicoNoBanco = tecnicoOptional.get();
            BeanUtils.copyProperties(tenicoAtualizacao, tecnicoNoBanco);
            definirEspecialidade(tenicoAtualizacao, tecnicoNoBanco);
            return tecnicoRepositorio.save(tecnicoNoBanco);
        }

        return null;
    }

    private void definirEspecialidade(TecnicoPayload tecnicoPayload, Tecnico tecnico) {
        Optional<Especialidade> optionalEspecialidade = especialidadeRepositorio.findById(tecnicoPayload.getIdEspecialidade());

        if (optionalEspecialidade.isPresent()) {
            tecnico.setEspecialidade(optionalEspecialidade.get());
        } else {
            throw new RuntimeException("Especialidade não encontrada");
        }
    }
}

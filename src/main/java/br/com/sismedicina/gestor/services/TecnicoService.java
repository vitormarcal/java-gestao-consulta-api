package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.dto.TecnicoPayload;
import br.com.sismedicina.gestor.model.Tecnico;
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

        return tecnicoRepositorio.save(tecnico);
    }

    public Optional<Tecnico> buscarPorId(Integer id) {
        return tecnicoRepositorio.findById(id);
    }

    public Tecnico atualizar(TecnicoPayload medicoAtualizacao) {
        Optional<Tecnico> medicoOptional = tecnicoRepositorio.findById(medicoAtualizacao.getId());

        if (medicoOptional.isPresent()) {
            Tecnico tecnicoNoBanco = medicoOptional.get();
            BeanUtils.copyProperties(medicoAtualizacao, tecnicoNoBanco);
            return tecnicoRepositorio.save(tecnicoNoBanco);
        }

        return null;
    }
}

package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.dto.MedicoCarga;
import br.com.sismedicina.gestor.model.Medico;
import br.com.sismedicina.gestor.repositorios.MedicoRepositorio;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepositorio medicoRepositorio;


    public List<Medico> filtrar() {
        return medicoRepositorio.findAll();
    }

    public Medico salvar(MedicoCarga medicoCarga) {

        Medico medico = new Medico();
        BeanUtils.copyProperties(medicoCarga, medico);

        medico.setDiasQueAtende(medicoCarga.getDiasQueAtende());

        return medicoRepositorio.save(medico);
    }

    public Optional<Medico> buscarPorId(Integer id) {
        return medicoRepositorio.findById(id);
    }

    public Medico atualizar(MedicoCarga medicoAtualizacao) {
        Optional<Medico> medicoOptional = medicoRepositorio.findById(medicoAtualizacao.getId());

        if (medicoOptional.isPresent()) {
            Medico medicoNoBanco = medicoOptional.get();
            BeanUtils.copyProperties(medicoAtualizacao, medicoNoBanco);
            return medicoRepositorio.save(medicoNoBanco);
        }

        return null;
    }
}

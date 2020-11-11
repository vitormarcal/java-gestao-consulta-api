package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.payload.request.TecnicoRequest;
import br.com.sismedicina.gestor.model.Especialidade;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.repositorios.EspecialidadeRepositorio;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import br.com.sismedicina.gestor.repositorios.UserRepositorio;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;
    @Autowired
    private EspecialidadeRepositorio especialidadeRepositorio;
    @Autowired
    private UserRepositorio userRepositorio;


    public List<Tecnico> filtrar() {
        return tecnicoRepositorio.findAll();
    }

    @Transactional
    public Tecnico salvar(TecnicoRequest tecnicoRequest, UserDetailsImpl userDetails) {

        if (tecnicoRepositorio.existsById(userDetails.getId())) {
            throw new RuntimeException("Técnico já cadastrado com usuário informado");
        }

        Tecnico tecnico = new Tecnico();
        BeanUtils.copyProperties(tecnicoRequest, tecnico);

        tecnico.setId(userDetails.getId());
        tecnico.setDiasQueAtende(tecnicoRequest.getDiasQueAtende());

        if (!tecnico.saoDatasValidas()) {
            throw new RuntimeException("As datas não estao em um intervalo valido");
        }

        Especialidade especialidade = especialidadeRepositorio.findById(tecnicoRequest.getIdEspecialidade())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        tecnico.setEspecialidade(especialidade);
        tecnico.abrirAgenda();
        Tecnico tecnicoSalvo = tecnicoRepositorio.save(tecnico);

        userRepositorio.setCadastroCompleto(userDetails.getId());
        return tecnicoSalvo;
    }

    public Optional<Tecnico> buscarPorId(Long id) {
        return tecnicoRepositorio.findById(id);
    }

    @Transactional
    public Tecnico atualizar(Long id, TecnicoRequest tenicoAtualizacao) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepositorio.findById(id);

        if (tecnicoOptional.isPresent()) {
            Tecnico tecnicoNoBanco = tecnicoOptional.get();
            BeanUtils.copyProperties(tenicoAtualizacao, tecnicoNoBanco);
            Especialidade especialidade = especialidadeRepositorio.findById(tenicoAtualizacao.getIdEspecialidade())
                    .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
            tecnicoNoBanco.setEspecialidade(especialidade);
            return tecnicoRepositorio.save(tecnicoNoBanco);
        }

        return null;
    }
}

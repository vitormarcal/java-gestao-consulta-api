package br.com.sismedicina.gestor.tecnico;

import br.com.sismedicina.gestor.auth.repositorio.UserRepositorio;
import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import br.com.sismedicina.gestor.especialidade.repositorio.EspecialidadeRepositorio;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import br.com.sismedicina.gestor.tecnico.model.Tecnico;
import br.com.sismedicina.gestor.tecnico.repositorio.TecnicoRepositorio;
import br.com.sismedicina.gestor.tecnico.request.TecnicoRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TecnicoService {

    private static final Logger logger = LoggerFactory.getLogger(TecnicoService.class);

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

        if (tecnicoRepositorio.existsByUserId(userDetails.getId())) {
            throw new RuntimeException("Técnico já cadastrado com usuário informado");
        }

        Tecnico tecnico = new Tecnico();
        BeanUtils.copyProperties(tecnicoRequest, tecnico);

        tecnico.setUserId(userDetails.getId());
        tecnico.setDiasQueAtende(tecnicoRequest.getDiasQueAtende());

        if (!tecnico.saoDatasValidas()) {
            throw new RuntimeException("As datas não estao em um intervalo valido");
        }

        userRepositorio.setCadastroCompleto(userDetails.getId());
        logger.info("Definido cadastro do user tecnico como completo {}", userDetails.getUsername());

        Especialidade especialidade = especialidadeRepositorio.findById(tecnicoRequest.getIdEspecialidade())
                .orElseThrow(() -> new RuntimeException("Especialidade não encontrada"));
        tecnico.setEspecialidade(especialidade);

        logger.info("Salvando tecnico na base para user {}", userDetails.getUsername());
        Tecnico tecnicoSalvo = tecnicoRepositorio.save(tecnico);
        logger.info("Salvado tecnico na base {}", tecnico.getId());

        return abrirAgenda(tecnicoSalvo);
    }

    private Tecnico abrirAgenda(Tecnico tecnicoSalvo) {
        logger.info("Abrindo agenda para tecnico {}", tecnicoSalvo.getId());
        tecnicoSalvo.abrirAgenda();
        logger.info("Salvando agenda para tecnico {}", tecnicoSalvo.getId());
        return tecnicoRepositorio.save(tecnicoSalvo);
    }

    public Optional<Tecnico> buscarPorId(Long id) {
        return tecnicoRepositorio.findById(id);
    }

    public Optional<Tecnico> buscarPorUsuario(Long userId) {
        return tecnicoRepositorio.findByUserId(userId);
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

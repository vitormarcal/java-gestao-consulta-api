package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.dto.TecnicoPayload;
import br.com.sismedicina.gestor.model.Especialidade;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.repositorios.EspecialidadeRepositorio;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import br.com.sismedicina.gestor.repositorios.UserRepository;
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
    private UserRepository userRepository;


    public List<Tecnico> filtrar() {
        return tecnicoRepositorio.findAll();
    }

    @Transactional
    public Tecnico salvar(TecnicoPayload tecnicoPayload, UserDetailsImpl userDetails) {

        if (tecnicoRepositorio.existsById(userDetails.getId())) {
            throw new RuntimeException("Técnico já cadastrado com usuário informado");
        }

        Tecnico tecnico = new Tecnico();
        BeanUtils.copyProperties(tecnicoPayload, tecnico);

        tecnico.setId(userDetails.getId());
        tecnico.setDiasQueAtende(tecnicoPayload.getDiasQueAtende());

        if (!tecnico.saoDatasValidas()) {
            throw new RuntimeException("As datas não estao em um intervalo valido");
        }

        definirEspecialidade(tecnicoPayload, tecnico);
        Tecnico tecnicoSalvo = tecnicoRepositorio.save(tecnico);

        userRepository.setCadastroCompleto(userDetails.getId());
        return tecnicoSalvo;
    }

    public Optional<Tecnico> buscarPorId(Long id) {
        return tecnicoRepositorio.findById(id);
    }

    @Transactional
    public Tecnico atualizar(Long id, TecnicoPayload tenicoAtualizacao) {
        Optional<Tecnico> tecnicoOptional = tecnicoRepositorio.findById(id);

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

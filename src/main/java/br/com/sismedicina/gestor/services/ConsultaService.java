package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.model.User;
import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.payload.response.ConsultaResponse;
import br.com.sismedicina.gestor.repositorios.ConsultaRepository;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import br.com.sismedicina.gestor.repositorios.UserRepository;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;
    @Autowired
    private UserRepository userRepository;

    public List<ConsultaDisponivelResponse> buscarAgendasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        List<Tecnico> tecnicos;
        if (filtro.getIdEspecialidade() == null) {
            tecnicos = tecnicoRepositorio.findAll();
        } else {
            tecnicos = tecnicoRepositorio.findAllByEspecialidade_Id(filtro.getIdEspecialidade());
        }

        Map<Long, Tecnico> tecnicoMap = new HashMap<>();
        for (Tecnico tecnico : tecnicos) {
            tecnicoMap.put(tecnico.getId(), tecnico);
        }

        List<Consulta> consultas = consultaRepository.findConsultasDisponiveis(filtro.getData(), tecnicoMap.keySet());


        List<ConsultaDisponivelResponse> responseList = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (tecnicoMap.containsKey(consulta.getTecnicoId())) {
                Tecnico tecnico = tecnicoMap.get(consulta.getTecnicoId());
                ConsultaDisponivelResponse response = new ConsultaDisponivelResponse();
                response.setIdConsulta(consulta.getId());
                response.setDataMarcada(consulta.getDataMarcada());
                response.setHorario(consulta.getInicioHorario());
                response.setEspecialidade(tecnico.getEspecialidade().getDescricao());
                response.setIdTecnico(tecnico.getId());
                responseList.add(response);
            }
        }

        return responseList;
    }

    @Transactional
    public Optional<Consulta> agendarParaEsteUsuario(Long idConsulta, UserDetailsImpl principal) {

        Optional<Consulta> optional = consultaRepository.findById(idConsulta);

        if (!optional.isPresent()) {
            return optional;
        }

        Consulta consulta = optional.get();

        if (consulta.getUserId() != null || consulta.getFimHorario() != null) {
            throw new RuntimeException("Consulta já está reservada!");
        }
        consulta.setUserId(principal.getId());

        Consulta consultaSalva = consultaRepository.save(consulta);

        return Optional.of(consultaSalva);
    }


    public Optional<ConsultaResponse> buscarPorId(Long idConsulta) {
        Optional<Consulta> optional = consultaRepository.findById(idConsulta);
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        Consulta consulta = optional.get();
        Tecnico tecnico = tecnicoRepositorio.findById(consulta.getTecnicoId()).orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
        User userTencnico = userRepository.findById(tecnico.getId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        User user = userRepository.findById(consulta.getUserId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return Optional.of(new ConsultaResponse(consulta, tecnico, userTencnico, user));

    }
}

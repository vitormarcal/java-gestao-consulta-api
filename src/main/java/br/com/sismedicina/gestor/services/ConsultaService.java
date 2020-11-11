package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.model.User;
import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.payload.response.ConsultaResponse;
import br.com.sismedicina.gestor.repositorios.ConsultaRepositorio;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import br.com.sismedicina.gestor.repositorios.UserRepositorio;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepositorio consultaRepositorio;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;
    @Autowired
    private UserRepositorio userRepositorio;

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

        List<Consulta> consultas = consultaRepositorio.findConsultasDisponiveis(filtro.getData(), tecnicoMap.keySet());


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

        Optional<Consulta> optional = consultaRepositorio.findById(idConsulta);

        if (!optional.isPresent()) {
            return optional;
        }

        Consulta consulta = optional.get();

        if (consulta.getUserId() != null || consulta.getFimHorario() != null) {
            throw new RuntimeException("Consulta já está reservada!");
        }
        consulta.setUserId(principal.getId());

        Consulta consultaSalva = consultaRepositorio.save(consulta);

        return Optional.of(consultaSalva);
    }


    public Optional<ConsultaResponse> buscarPorId(Long idConsulta) {
        Optional<Consulta> optional = consultaRepositorio.findById(idConsulta);
        if (!optional.isPresent()) {
            return Optional.empty();
        }

        Consulta consulta = optional.get();
        Tecnico tecnico = tecnicoRepositorio.findById(consulta.getTecnicoId()).orElseThrow(() -> new RuntimeException("Técnico não encontrado"));
        User userTencnico = userRepositorio.findById(tecnico.getId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        User user = userRepositorio.findById(consulta.getUserId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return Optional.of(new ConsultaResponse(consulta, tecnico, userTencnico, user));

    }

    @Transactional
    public Optional<ConsultaResponse> finalizarConsulta(Long idConsulta, UserDetailsImpl principal) {
        Optional<ConsultaResponse> optional = buscarPorId(idConsulta);

        if (!optional.isPresent()) {
            return Optional.empty();
        }

        ConsultaResponse consultaResponse = optional.get();

        if (!principal.getId().equals(consultaResponse.getIdUsuario()) &&
                !principal.getId().equals(consultaResponse.getIdUsuarioTecnico())) {
            return Optional.empty();
        }
        LocalTime horaFim = LocalTime.now();
        int i = consultaRepositorio.finalizarConsulta(idConsulta, horaFim);
        if (i == 1) {
            consultaResponse.setFimConsulta(LocalDateTime.of(LocalDate.now(), horaFim));
            return Optional.of(consultaResponse);
        }
        return Optional.empty();

    }

}

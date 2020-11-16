package br.com.sismedicina.gestor.consulta;

import br.com.sismedicina.gestor.consulta.model.Consulta;
import br.com.sismedicina.gestor.consulta.repositorio.ConsultaRepositorio;
import br.com.sismedicina.gestor.consulta.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.consulta.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.consulta.response.ConsultaResponse;
import br.com.sismedicina.gestor.tecnico.model.Tecnico;
import br.com.sismedicina.gestor.auth.model.User;
import br.com.sismedicina.gestor.tecnico.repositorio.TecnicoRepositorio;
import br.com.sismedicina.gestor.auth.repositorio.UserRepositorio;
import br.com.sismedicina.gestor.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

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

        List<Consulta> consultas = consultaRepositorio.findConsultasDisponiveis(filtro.getData(),
                tecnicos.stream().map(Tecnico::getId).collect(Collectors.toList()));

        return getConsultaDisponivelResponses(consultas, tecnicos);
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
        User userTencnico = userRepositorio.findById(tecnico.getUserId()).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        User user;
        if (consulta.getUserId() == null) {
            user = new User();
        } else {
            user = userRepositorio.findById(consulta.getUserId()).orElse(new User());
        }

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

    public List<ConsultaDisponivelResponse> buscarConsultasDoUsuario(UserDetailsImpl userDetails) {

        List<Consulta> consultas = tecnicoRepositorio.findByUserId(userDetails.getId())
                .map(tecnico -> consultaRepositorio.findByTecnicoId(tecnico.getId()))
                .orElseGet(() -> consultaRepositorio.findByUserId(userDetails.getId()));

        List<Tecnico> tecnicos = tecnicoRepositorio.findAllById(consultas.stream().map(Consulta::getTecnicoId).collect(Collectors.toList()));

        return getConsultaDisponivelResponses(consultas, tecnicos);
    }

    private List<ConsultaDisponivelResponse> getConsultaDisponivelResponses(List<Consulta> consultas, List<Tecnico> tecnicos) {
        Map<Long, Tecnico> tecnicoMap = new HashMap<>();
        for (Tecnico tecnico : tecnicos) {
            tecnicoMap.put(tecnico.getId(), tecnico);
        }

        List<ConsultaDisponivelResponse> responseList = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (tecnicoMap.containsKey(consulta.getTecnicoId())) {
                Tecnico tecnico = tecnicoMap.get(consulta.getTecnicoId());
                ConsultaDisponivelResponse response = new ConsultaDisponivelResponse();
                response.setIdConsulta(consulta.getId());
                response.setDataMarcada(consulta.getDataMarcada());
                response.setFimHorario(consulta.getFimHorario());
                response.setHorario(consulta.getInicioHorario());
                response.setEspecialidade(tecnico.getEspecialidade().getDescricao());
                response.setIdTecnico(tecnico.getId());
                response.setIdUsuario(consulta.getUserId());
                responseList.add(response);
            }
        }


        return responseList;
    }

    public void removerConsultaDisponivel(Long idConsulta) {
         consultaRepositorio.deleteById(idConsulta);
    }
}

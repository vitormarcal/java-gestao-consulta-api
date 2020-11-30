package br.com.vitormarcal.atendimento_agendado.consulta;

import br.com.vitormarcal.atendimento_agendado.auth.model.User;
import br.com.vitormarcal.atendimento_agendado.auth.repositorio.UserRepositorio;
import br.com.vitormarcal.atendimento_agendado.consulta.model.Consulta;
import br.com.vitormarcal.atendimento_agendado.consulta.repositorio.ConsultaRepositorio;
import br.com.vitormarcal.atendimento_agendado.consulta.request.FiltroConsultaDisponivelRequest;
import br.com.vitormarcal.atendimento_agendado.consulta.response.ConsultaDisponivelResponse;
import br.com.vitormarcal.atendimento_agendado.consulta.response.ConsultaResponse;
import br.com.vitormarcal.atendimento_agendado.security.services.UserDetailsImpl;
import br.com.vitormarcal.atendimento_agendado.tecnico.model.Tecnico;
import br.com.vitormarcal.atendimento_agendado.tecnico.repositorio.TecnicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepositorio consultaRepositorio;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;
    @Autowired
    private UserRepositorio userRepositorio;

    public List<ConsultaDisponivelResponse> buscarAgendasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        List<Consulta> consultas = consultaRepositorio.findConsultasDisponiveis(filtro.getData(),
                filtro.getIdEspecialidade());

        return getConsultaDisponivelResponses(consultas);
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

        List<Consulta> consultas = consultaRepositorio.findByTecnicoIdAndInicioHorarioAndDataMarcada(consulta.getTecnicoId(), consulta.getInicioHorario(), consulta.getDataMarcada());

        consultas.remove(consulta);

        if (consultas.stream().anyMatch(e -> e.getUserId() != null)) {
            throw new RuntimeException("Consulta não está mais disponivel!");
        }

        consulta.setUserId(principal.getId());
        consultaRepositorio.deleteInBatch(consultas);
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

        return getConsultaDisponivelResponses(consultas);
    }

    private List<ConsultaDisponivelResponse> getConsultaDisponivelResponses(List<Consulta> consultas) {
        List<ConsultaDisponivelResponse> responseList = new ArrayList<>();
        for (Consulta consulta : consultas) {
            ConsultaDisponivelResponse response = new ConsultaDisponivelResponse();
            response.setIdConsulta(consulta.getId());
            response.setDataMarcada(consulta.getDataMarcada());
            response.setFimHorario(consulta.getFimHorario());
            response.setHorario(consulta.getInicioHorario());
            response.setEspecialidade(consulta.getEspecialidade().getDescricao());
            response.setIdTecnico(consulta.getTecnicoId());
            response.setIdUsuario(consulta.getUserId());
            responseList.add(response);

        }

        return responseList;
    }

    public void removerConsultaDisponivel(Long idConsulta) {
        consultaRepositorio.deleteById(idConsulta);
    }
}

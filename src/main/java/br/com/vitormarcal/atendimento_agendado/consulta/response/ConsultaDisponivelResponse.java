package br.com.vitormarcal.atendimento_agendado.consulta.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaDisponivelResponse {
    private Long idConsulta;
    private Long idTecnico;
    private Long idUsuario;
    private String especialidade;
    private LocalDate dataMarcada;
    private LocalTime horario;
    private LocalTime fimHorario;

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDate getDataMarcada() {
        return dataMarcada;
    }

    public void setDataMarcada(LocalDate dataMarcada) {
        this.dataMarcada = dataMarcada;
    }

    public LocalTime getHorario() {
        return horario;
    }

    public void setHorario(LocalTime horario) {
        this.horario = horario;
    }

    public LocalTime getFimHorario() {
        return fimHorario;
    }

    public void setFimHorario(LocalTime fimHorario) {
        this.fimHorario = fimHorario;
    }
}

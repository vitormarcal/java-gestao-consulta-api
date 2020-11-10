package br.com.sismedicina.gestor.payload.response;

import br.com.sismedicina.gestor.model.Especialidade;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaDisponivelResponse {
    private Long idTecnico;
    private String especialidade;
    private LocalDate dataMarcada;
    private LocalTime horario;


    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
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
}

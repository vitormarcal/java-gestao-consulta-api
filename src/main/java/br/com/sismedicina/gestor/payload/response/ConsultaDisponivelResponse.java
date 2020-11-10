package br.com.sismedicina.gestor.payload.response;

import br.com.sismedicina.gestor.model.Especialidade;

import java.time.LocalDate;
import java.time.LocalTime;

public class ConsultaDisponivelResponse {
    private Long idTecnico;
    private Especialidade especialidade;
    private LocalDate dataMarcada;
    private LocalTime dataIncio;


    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDate getDataMarcada() {
        return dataMarcada;
    }

    public void setDataMarcada(LocalDate dataMarcada) {
        this.dataMarcada = dataMarcada;
    }

    public LocalTime getDataIncio() {
        return dataIncio;
    }

    public void setDataIncio(LocalTime dataIncio) {
        this.dataIncio = dataIncio;
    }
}

package br.com.vitormarcal.atendimento_agendado.consulta.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class FiltroConsultaDisponivelRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate data;
    private Integer idEspecialidade;


    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public Integer getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Integer idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }
}

package br.com.sismedicina.gestor.payload.request;

import java.time.LocalDate;

public class FiltroConsultaDisponivelRequest {
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

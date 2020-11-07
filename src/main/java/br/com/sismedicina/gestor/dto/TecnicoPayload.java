package br.com.sismedicina.gestor.dto;

import br.com.sismedicina.gestor.model.DiasDaSemana;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public class TecnicoPayload {

    private Integer id;

    @JsonIgnore
    @NotNull
    private Integer idEspecialidade;

    private LocalTime duracaoAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;

    private List<DiasDaSemana> diasQueAtende;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Integer idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public LocalTime getInicioAtendimento() {
        return inicioAtendimento;
    }

    public void setInicioAtendimento(LocalTime inicioAtendimento) {
        this.inicioAtendimento = inicioAtendimento;
    }

    public LocalTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }

    public LocalTime getSaidaDescanso() {
        return saidaDescanso;
    }

    public void setSaidaDescanso(LocalTime saidaDescanso) {
        this.saidaDescanso = saidaDescanso;
    }

    public LocalTime getVoltaDescanso() {
        return voltaDescanso;
    }

    public void setVoltaDescanso(LocalTime voltaDescanso) {
        this.voltaDescanso = voltaDescanso;
    }

    public List<DiasDaSemana> getDiasQueAtende() {
        return diasQueAtende;
    }

    public void setDiasQueAtende(List<DiasDaSemana> diasQueAtende) {
        this.diasQueAtende = diasQueAtende;
    }

    public LocalTime getDuracaoAtendimento() {
        return duracaoAtendimento;
    }

    public void setDuracaoAtendimento(LocalTime duracaoAtendimento) {
        this.duracaoAtendimento = duracaoAtendimento;
    }
}

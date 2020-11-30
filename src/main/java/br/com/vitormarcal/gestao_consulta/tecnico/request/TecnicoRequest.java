package br.com.vitormarcal.gestao_consulta.tecnico.request;

import br.com.vitormarcal.gestao_consulta.tecnico.model.DiaDaSemana;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;
import java.util.List;

public class TecnicoRequest {

    @NotNull
    private List<Integer> idEspecialidade;

    private Integer duracaoAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;

    private List<DiaDaSemana> diasQueAtende;

    public List<Integer> getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(List<Integer> idEspecialidade) {
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

    public List<DiaDaSemana> getDiasQueAtende() {
        return diasQueAtende;
    }

    public void setDiasQueAtende(List<DiaDaSemana> diasQueAtende) {
        this.diasQueAtende = diasQueAtende;
    }

    public Integer getDuracaoAtendimento() {
        return duracaoAtendimento;
    }

    public void setDuracaoAtendimento(Integer duracaoAtendimento) {
        this.duracaoAtendimento = duracaoAtendimento;
    }
}

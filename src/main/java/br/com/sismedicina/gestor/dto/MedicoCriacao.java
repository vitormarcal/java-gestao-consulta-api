package br.com.sismedicina.gestor.dto;

import br.com.sismedicina.gestor.model.DiasDaSemana;
import br.com.sismedicina.gestor.model.Especialidade;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalTime;
import java.util.List;

public class MedicoCriacao {

    private String nome;

    @JsonIgnore
    private Especialidade especialidade;

    private LocalTime inicioAtendimento;
    private LocalTime fimAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;

    private List<DiasDaSemana> diasQueAtende;


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
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
}

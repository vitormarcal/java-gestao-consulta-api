package br.com.sismedicina.gestor.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idEspecialidade")
    private Especialidade especialidade;

    private Integer duracaoAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;
    private LocalTime fimAtendimento;

    private String diasQueAtende;


    public void setDiasQueAtende(List<DiasDaSemana> diasQueAtende) {
        this.diasQueAtende = diasQueAtende.stream()
                .map(DiasDaSemana::name)
                .collect(Collectors.joining(","));
    }

    public boolean saoDatasValidas() {
        return inicioAtendimento.isBefore(saidaDescanso) &&
                saidaDescanso.isBefore(voltaDescanso) &&
                voltaDescanso.isBefore(fimAtendimento);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }

    public String getDiasQueAtende() {
        return diasQueAtende;
    }

    public void setDiasQueAtende(String diasQueAtende) {
        this.diasQueAtende = diasQueAtende;
    }

    public Integer getDuracaoAtendimento() {
        return duracaoAtendimento;
    }

    public void setDuracaoAtendimento(Integer duracaoAtendimento) {
        this.duracaoAtendimento = duracaoAtendimento;
    }
}

package br.com.sismedicina.gestor.model;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String nome;

    @ManyToOne
    @JoinColumn(name = "idEspecialidade")
    private Especialidade especialidade;

    private LocalTime inicioAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;
    private LocalDateTime fimAtendimento;

    private String diasQueAtende;


    public void setDiasQueAtende(List<DiasDaSemana> diasQueAtende) {
        this.diasQueAtende = diasQueAtende.stream()
                .map(DiasDaSemana::name)
                .collect(Collectors.joining(","));
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public LocalTime getSaidaDescanso() {
        return saidaDescanso;
    }

    public void setSaidaDescanso(LocalTime saidaDescanso) {
        this.saidaDescanso = saidaDescanso;
    }

    public LocalTime getVoltaDescanso() {
        return voltaDescanso;
    }

    public void setVoltaDescanso(LocalTime voltaDesacanso) {
        this.voltaDescanso = voltaDesacanso;
    }

    public LocalDateTime getFimAtendimento() {
        return fimAtendimento;
    }

    public void setFimAtendimento(LocalDateTime fimAtendimento) {
        this.fimAtendimento = fimAtendimento;
    }

    public String getDiasQueAtende() {
        return diasQueAtende;
    }

    public void setDiasQueAtende(String diasQueAtende) {
        this.diasQueAtende = diasQueAtende;
    }
}

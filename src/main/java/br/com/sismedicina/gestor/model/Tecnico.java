package br.com.sismedicina.gestor.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nome;
    private String telefone;

    @ManyToOne
    @JoinColumn(name = "idEspecialidade")
    private Especialidade especialidade;

    private LocalTime duracaoAtendimento;
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
        return inicioAtendimento.isAfter(saidaDescanso) &&
                saidaDescanso.isAfter(voltaDescanso) &&
                voltaDescanso.isAfter(fimAtendimento);
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


    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalTime getDuracaoAtendimento() {
        return duracaoAtendimento;
    }

    public void setDuracaoAtendimento(LocalTime duracaoAtendimento) {
        this.duracaoAtendimento = duracaoAtendimento;
    }
}

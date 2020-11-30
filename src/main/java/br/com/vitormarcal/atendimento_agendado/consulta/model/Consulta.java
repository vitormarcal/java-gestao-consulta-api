package br.com.vitormarcal.atendimento_agendado.consulta.model;

import br.com.vitormarcal.atendimento_agendado.especialidade.model.Especialidade;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name = "Consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_marcada")
    private LocalDate dataMarcada;

    @Column(name = "inicio_horario")
    private LocalTime inicioHorario;
    @Column(name = "fim_horario")
    private LocalTime fimHorario;

    @JoinColumn(name = "user_id")
    private Long userId;


    @JoinColumn(name = "tecnico_id", nullable = false)
    private Long tecnicoId;

    @ManyToOne
    @JoinColumn(name = "especialidade_id", nullable = true)
    private Especialidade especialidade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataMarcada() {
        return dataMarcada;
    }

    public void setDataMarcada(LocalDate dataMarcada) {
        this.dataMarcada = dataMarcada;
    }

    public LocalTime getInicioHorario() {
        return inicioHorario;
    }

    public void setInicioHorario(LocalTime inicioHorario) {
        this.inicioHorario = inicioHorario;
    }

    public LocalTime getFimHorario() {
        return fimHorario;
    }

    public void setFimHorario(LocalTime fimHorario) {
        this.fimHorario = fimHorario;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }

    public void setTecnicoId(Long tecnicoId) {
        this.tecnicoId = tecnicoId;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Consulta consulta = (Consulta) o;
        return Objects.equals(id, consulta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

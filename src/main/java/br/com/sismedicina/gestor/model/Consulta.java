package br.com.sismedicina.gestor.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inicio_horario")
    private LocalDateTime inicioHorario;
    @Column(name = "fim_horario")
    private LocalDateTime fimHorario;

    @JoinColumn(name = "user_id")
    private Long userId;


    @JoinColumn(name = "tecnico_id", nullable = false)
    private Long tecnicoId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getInicioHorario() {
        return inicioHorario;
    }

    public void setInicioHorario(LocalDateTime inicioHorario) {
        this.inicioHorario = inicioHorario;
    }

    public LocalDateTime getFimHorario() {
        return fimHorario;
    }

    public void setFimHorario(LocalDateTime fimHorario) {
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
}

package br.com.vitormarcal.gestao_consulta.chat.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mensagem")
public class Mensagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "consulta_id", nullable = false)
    private Long consultaId;

    @NotBlank
    private String sessionId;

    @NotBlank
    @Size(max = 512)
    @Lob
    @Column(length=512, nullable = false)
    private String texto;
    private String de;
    private String para;
    private LocalDateTime em;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getConsultaId() {
        return consultaId;
    }

    public void setConsultaId(Long consultaId) {
        this.consultaId = consultaId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public LocalDateTime getEm() {
        return em;
    }

    public void setEm(LocalDateTime em) {
        this.em = em;
    }
}

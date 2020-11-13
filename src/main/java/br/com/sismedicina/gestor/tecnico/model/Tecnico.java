package br.com.sismedicina.gestor.tecnico.model;

import br.com.sismedicina.gestor.consulta.model.Consulta;
import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.sismedicina.gestor.tecnico.model.DiaDaSemana.getDayOfWeek;

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

    @Where(clause = "fim_horario IS NULL")
    @OneToMany(mappedBy = "tecnicoId", cascade = CascadeType.ALL)
    private List<Consulta> consultas = new ArrayList<>();


    public void setDiasQueAtende(List<DiaDaSemana> diasQueAtende) {
        this.diasQueAtende = diasQueAtende.stream()
                .map(DiaDaSemana::name)
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

    public List<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(List<Consulta> consultas) {
        this.consultas = consultas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnico tecnico = (Tecnico) o;
        return Objects.equals(id, tecnico.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void abrirAgenda() {
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = agora.toLocalDate();
        LocalDate fimDoMes = agora.withDayOfMonth(hoje.lengthOfMonth()).toLocalDate();

        LocalTime inicio = agora.toLocalTime();

        List<DayOfWeek> diasQueAtende = getDayOfWeek(this.diasQueAtende.split(","));

        while (!hoje.equals(fimDoMes)) {
            LocalTime fim = inicio.plusMinutes(duracaoAtendimento);
            DayOfWeek dayOfWeek = hoje.getDayOfWeek();
            if (!diasQueAtende.contains(dayOfWeek)) {
                hoje = hoje.plusDays(1);
                continue;
            }

            if (inicio.isBefore(this.inicioAtendimento) || inicio.equals(this.inicioAtendimento)) {
                inicio = this.inicioAtendimento.plusMinutes(duracaoAtendimento);
                fim = inicio.plusMinutes(duracaoAtendimento);
            }

            if (inicio.equals(saidaDescanso) || inicio.equals(voltaDescanso) ||
                    (inicio.isAfter(saidaDescanso) && inicio.isBefore(voltaDescanso)) ||
                    (fim.isAfter(saidaDescanso) && fim.isBefore(voltaDescanso))) {
                inicio = this.voltaDescanso.plusMinutes(duracaoAtendimento);
            }

            if (inicio.equals(this.fimAtendimento) ||
                    inicio.isAfter(this.fimAtendimento) ||
                    inicio.isAfter(this.fimAtendimento.minusMinutes(duracaoAtendimento))) {
                inicio = this.inicioAtendimento.plusMinutes(duracaoAtendimento);
                hoje = hoje.plusDays(1);
            }

            Consulta consulta = new Consulta();
            consulta.setDataMarcada(hoje);
            consulta.setInicioHorario(inicio);
            consulta.setTecnicoId(this.getId());
            inicio = inicio.plusMinutes(duracaoAtendimento + 10);
            this.consultas.add(consulta);

        }


    }
}

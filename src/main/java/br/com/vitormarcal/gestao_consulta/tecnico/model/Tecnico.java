package br.com.vitormarcal.gestao_consulta.tecnico.model;

import br.com.vitormarcal.gestao_consulta.consulta.model.Consulta;
import br.com.vitormarcal.gestao_consulta.especialidade.model.Especialidade;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static br.com.vitormarcal.gestao_consulta.tecnico.model.DiaDaSemana.getDayOfWeek;

@Entity
public class Tecnico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tecnico_especialidades",
            joinColumns = @JoinColumn(name = "tecnico_id"),
            inverseJoinColumns = @JoinColumn(name = "especialidade_id"))
    private Set<Especialidade> especialidades = new HashSet<>();

    @JoinColumn(name = "user_id", nullable = false)
    private Long userId;

    private Integer duracaoAtendimento;
    private LocalTime inicioAtendimento;
    private LocalTime saidaDescanso;
    private LocalTime voltaDescanso;
    private LocalTime fimAtendimento;

    private String diasQueAtende;

    @Where(clause = "fim_horario IS NULL")
    @OneToMany(mappedBy = "tecnicoId", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }


    public Set<Especialidade> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(Set<Especialidade> especialidades) {
        this.especialidades = especialidades;
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
        int TEMPO_ENTRE_CONSULTAS = 5;
        LocalDateTime agora = LocalDateTime.now();
        LocalDate hoje = agora.toLocalDate();
        LocalDate fimDoMes = agora.withDayOfMonth(hoje.lengthOfMonth()).toLocalDate();

        LocalTime inicio = agora.toLocalTime();

        List<DayOfWeek> diasQueAtende = getDayOfWeek(this.diasQueAtende.split(","));

        this.consultas.removeIf(consulta -> consulta.getUserId() == null);
        while (!hoje.equals(fimDoMes) || (hoje.equals(fimDoMes) && !(inicio.equals(this.fimAtendimento) || inicio.isAfter(this.fimAtendimento)))) {
            LocalTime fim = inicio.plusMinutes(duracaoAtendimento);
            DayOfWeek dayOfWeek = hoje.getDayOfWeek();
            if (!diasQueAtende.contains(dayOfWeek)) {
                hoje = hoje.plusDays(1);
                continue;
            }

            if (inicio.isBefore(this.inicioAtendimento)) {
                inicio = this.inicioAtendimento;
                fim = inicio.plusMinutes(duracaoAtendimento);
            }

            if (inicio.equals(saidaDescanso) || inicio.equals(voltaDescanso) ||
                    (inicio.isAfter(saidaDescanso) && inicio.isBefore(voltaDescanso)) ||
                    (fim.isAfter(saidaDescanso) && fim.isBefore(voltaDescanso))) {
                inicio = this.voltaDescanso.plusMinutes(TEMPO_ENTRE_CONSULTAS);
            }

            if (inicio.equals(this.fimAtendimento) ||
                    inicio.isAfter(this.fimAtendimento) ||
                    inicio.isAfter(this.fimAtendimento.minusMinutes(duracaoAtendimento))) {
                inicio = this.inicioAtendimento;
                hoje = hoje.plusDays(1);
                continue;
            }

            if (chocouComConsultaExistene(hoje, inicio)) {
                inicio = inicio.plusMinutes(duracaoAtendimento);
                continue;
            }

            for (Especialidade especialidade : this.especialidades) {
                Consulta consulta = new Consulta();
                consulta.setDataMarcada(hoje);
                consulta.setInicioHorario(inicio);
                consulta.setTecnicoId(this.getId());
                consulta.setEspecialidade(especialidade);
                this.consultas.add(consulta);
            }
            inicio = inicio.plusMinutes(duracaoAtendimento + TEMPO_ENTRE_CONSULTAS);
        }


    }

    private boolean chocouComConsultaExistene(LocalDate hoje, LocalTime inicio) {
        return this.consultas.stream()
                .filter(consulta -> consulta.getDataMarcada().equals(hoje))
                .anyMatch(consulta -> consulta.getInicioHorario().equals(inicio) ||
                                (inicio.isAfter(consulta.getInicioHorario()) && inicio.isBefore(consulta.getInicioHorario().plusMinutes(duracaoAtendimento)))
                        );
    }
}

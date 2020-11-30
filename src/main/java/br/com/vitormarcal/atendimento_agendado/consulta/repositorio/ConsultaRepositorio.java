package br.com.vitormarcal.atendimento_agendado.consulta.repositorio;

import br.com.vitormarcal.atendimento_agendado.consulta.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Long>, PagingAndSortingRepository<Consulta, Long> {


    @Query("SELECT c FROM Consulta c" +
            " WHERE 0=0 " +
            " AND c.userId IS NULL " +
            " AND (:especialidadeId IS NULL OR c.especialidade.id = :especialidadeId) " +
            " AND (:data IS NULL OR c.dataMarcada = :data) " +
            " ORDER BY c.dataMarcada, c.inicioHorario ASC")
    List<Consulta> findConsultasDisponiveis(@Param(value = "data") LocalDate data, @Param(value = "especialidadeId") Integer especialidadeId);

    Optional<Consulta> findFirstByUserIdAndTecnicoIdAndAndFimHorarioIsNull(Long userId, Long tecnicoId);

    List<Consulta> findByUserId(Long userId);

    List<Consulta> findByTecnicoId(Long tecnicoId);

    List<Consulta> findByTecnicoIdAndInicioHorarioAndDataMarcada(Long tecnicoId, LocalTime inicioHorario, LocalDate data);

    @Modifying
    @Query("UPDATE Consulta c SET c.fimHorario=:hora WHERE c.id=:idConsulta")
    int finalizarConsulta(@Param(value = "idConsulta") Long idConsulta, @Param(value = "hora") LocalTime hora);


    void deleteAllByIdIn(List<Long> id);

    @Query("SELECT c.id FROM Consulta c WHERE c.fimHorario IS NULL")
    List<Long> findIdByFimHorarioIsNull();

}

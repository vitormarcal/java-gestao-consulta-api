package br.com.sismedicina.gestor.consulta.repositorio;

import br.com.sismedicina.gestor.consulta.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ConsultaRepositorio extends JpaRepository<Consulta, Long> {


    @Query("SELECT c FROM Consulta c" +
            " WHERE 0=0 " +
            " AND c.tecnicoId IN (:tecnicoId) " +
            " AND c.userId IS NULL " +
            " AND (:data IS NULL OR c.dataMarcada = :data) " +
            " ORDER BY c.dataMarcada, c.inicioHorario ASC")
    List<Consulta> findConsultasDisponiveis(@Param(value = "data") LocalDate data, @Param(value = "tecnicoId") Collection<Long> tecnicoId);


    Optional<Consulta> findFirstByUserIdAndTecnicoIdAndAndFimHorarioIsNull(Long userId, Long tecnicoId);

    @Query("SELECT c FROM Consulta c WHERE c.userId = :id OR c.tecnicoId = :id")
    List<Consulta> findByUserIdOrTecnicoId(@Param(value = "id") Long id);

    @Modifying
    @Query("UPDATE Consulta c SET c.fimHorario=:hora WHERE c.id=:idConsulta")
    int finalizarConsulta(@Param(value = "idConsulta") Long idConsulta, @Param(value = "hora") LocalTime hora);

}
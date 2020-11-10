package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {


    @Query("SELECT c FROM Consulta c WHERE c.tecnicoId IN (:tecnicoId) AND :data = null OR c.dataMarcada = :data ")
    List<Consulta> findConsultasDisponiveis(@Param(value = "data") LocalDate data, @Param(value = "tecnicoId") Collection<Long> tecnicoId);

}

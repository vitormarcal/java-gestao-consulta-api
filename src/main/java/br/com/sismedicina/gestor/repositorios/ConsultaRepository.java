package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    List<Consulta> findAllByInicioHorarioGreaterThan(LocalDateTime localDateTime);

}

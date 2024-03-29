package br.com.vitormarcal.gestao_consulta.tecnico.repositorio;

import br.com.vitormarcal.gestao_consulta.tecnico.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TecnicoRepositorio extends JpaRepository<Tecnico, Long>, PagingAndSortingRepository<Tecnico, Long> {

    Optional<Tecnico> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

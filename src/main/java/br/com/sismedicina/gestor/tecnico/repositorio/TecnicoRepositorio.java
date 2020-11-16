package br.com.sismedicina.gestor.tecnico.repositorio;

import br.com.sismedicina.gestor.tecnico.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TecnicoRepositorio extends JpaRepository<Tecnico, Long>, PagingAndSortingRepository<Tecnico, Long> {

    Optional<Tecnico> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Tecnico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TecnicoRepositorio extends JpaRepository<Tecnico, Long> {


    List<Tecnico> findAllByEspecialidade_Id(Integer idEspecialidade);

}

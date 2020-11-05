package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepositorio extends JpaRepository<Medico, Integer> {

}

package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Especialidade;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EspecialidadeRepositorio extends PagingAndSortingRepository<Especialidade, Integer> {
}

package br.com.sismedicina.gestor.especialidade.repositorio;

import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface EspecialidadeRepositorio extends PagingAndSortingRepository<Especialidade, Integer> {
}

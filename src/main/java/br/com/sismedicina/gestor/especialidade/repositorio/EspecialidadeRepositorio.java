package br.com.sismedicina.gestor.especialidade.repositorio;

import br.com.sismedicina.gestor.especialidade.model.Especialidade;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface EspecialidadeRepositorio extends PagingAndSortingRepository<Especialidade, Integer> {


    Set<Especialidade> findByIdIn(List<Integer> idEspecialidade);
}

package br.com.vitormarcal.gestao_consulta.especialidade.repositorio;

import br.com.vitormarcal.gestao_consulta.especialidade.model.Especialidade;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface EspecialidadeRepositorio extends PagingAndSortingRepository<Especialidade, Integer> {


    Set<Especialidade> findByIdIn(List<Integer> idEspecialidade);
}

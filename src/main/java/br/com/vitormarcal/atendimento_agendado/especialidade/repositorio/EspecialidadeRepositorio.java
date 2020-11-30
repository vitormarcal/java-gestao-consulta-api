package br.com.vitormarcal.atendimento_agendado.especialidade.repositorio;

import br.com.vitormarcal.atendimento_agendado.especialidade.model.Especialidade;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

public interface EspecialidadeRepositorio extends PagingAndSortingRepository<Especialidade, Integer> {


    Set<Especialidade> findByIdIn(List<Integer> idEspecialidade);
}

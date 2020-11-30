package br.com.vitormarcal.atendimento_agendado.auth.repositorio;

import br.com.vitormarcal.atendimento_agendado.auth.model.ERole;
import br.com.vitormarcal.atendimento_agendado.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositorio extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
package br.com.vitormarcal.gestao_consulta.auth.repositorio;

import br.com.vitormarcal.gestao_consulta.auth.model.ERole;
import br.com.vitormarcal.gestao_consulta.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositorio extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
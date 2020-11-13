package br.com.sismedicina.gestor.auth.repositorio;

import br.com.sismedicina.gestor.auth.model.ERole;
import br.com.sismedicina.gestor.auth.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepositorio extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
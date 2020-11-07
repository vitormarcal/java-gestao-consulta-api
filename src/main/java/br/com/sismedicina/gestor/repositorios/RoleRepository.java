package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.ERole;
import br.com.sismedicina.gestor.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensagemRepositorio extends JpaRepository<Mensagem, Long> {


    @Query("SELECT m FROM Mensagem m WHERE m.consultaId = :consultaId  AND (m.de = :username OR m.para = :username)")
    List<Mensagem> findByConsultaAndUsername(@Param(value = "consultaId") Long consultaId, @Param(value = "username") String username);

}

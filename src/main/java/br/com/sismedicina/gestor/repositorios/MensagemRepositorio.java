package br.com.sismedicina.gestor.repositorios;

import br.com.sismedicina.gestor.model.Mensagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MensagemRepositorio extends JpaRepository<Mensagem, Long> {
}

package br.com.sismedicina.gestor.tarefa;

import br.com.sismedicina.gestor.consulta.model.Consulta;
import br.com.sismedicina.gestor.consulta.repositorio.ConsultaRepositorio;
import br.com.sismedicina.gestor.tecnico.model.Tecnico;
import br.com.sismedicina.gestor.tecnico.repositorio.TecnicoRepositorio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
public class AberturaAgenda {

    private static Logger logger = LoggerFactory.getLogger(AberturaAgenda.class);

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Autowired
    private ConsultaRepositorio consultaRepositorio;


    @Scheduled(cron = "0 0 0 1 * * ")
    @Transactional
    public void abrirTodasAgendas() {
        consultaRepositorio.deleteAllByFimHorarioIsNull();
        consultaRepositorio.flush();
        logger.info("Deletado todas as consultas  não finalizadas");

        Pageable pageable = PageRequest.of(0, 1);

        boolean temProximo;

        do {
            Page<Tecnico> page = tecnicoRepositorio.findAll(pageable);
            if (page.hasContent()) {
                List<Tecnico> content = page.getContent();
                List<Consulta> paraSalvar = new ArrayList<>();
                for (Tecnico tecnico : content) {
                    tecnico.abrirAgenda();
                    paraSalvar.addAll(tecnico.getConsultas());
                }

                consultaRepositorio.saveAll(paraSalvar);
            }
            pageable = page.nextPageable();
            temProximo = page.hasNext();
        } while (temProximo);

    }
}

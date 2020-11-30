package br.com.vitormarcal.atendimento_agendado.tarefa;

import br.com.vitormarcal.atendimento_agendado.chat.repositorio.MensagemRepositorio;
import br.com.vitormarcal.atendimento_agendado.consulta.model.Consulta;
import br.com.vitormarcal.atendimento_agendado.consulta.repositorio.ConsultaRepositorio;
import br.com.vitormarcal.atendimento_agendado.tecnico.model.Tecnico;
import br.com.vitormarcal.atendimento_agendado.tecnico.repositorio.TecnicoRepositorio;
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

    private static final Logger logger = LoggerFactory.getLogger(AberturaAgenda.class);

    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    @Autowired
    private ConsultaRepositorio consultaRepositorio;

    @Autowired
    private MensagemRepositorio mensagemRepositorio;


    @Scheduled(cron = "0 0 0 1 * * ")
    @Transactional
    public void abrirTodasAgendas() {
        removerConsultasNaoFinalizadas();

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

    private void removerConsultasNaoFinalizadas() {
        List<Long> lista = consultaRepositorio.findIdByFimHorarioIsNull();
        if (!lista.isEmpty()) {
            mensagemRepositorio.deleteAllByConsultaIdIn(lista);
            mensagemRepositorio.flush();
            consultaRepositorio.deleteAllByIdIn(lista);
            consultaRepositorio.flush();
        }

        logger.info("Deletado todas as consultas  não finalizadas");
    }
}

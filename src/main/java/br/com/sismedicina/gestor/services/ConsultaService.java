package br.com.sismedicina.gestor.services;

import br.com.sismedicina.gestor.model.Consulta;
import br.com.sismedicina.gestor.model.Tecnico;
import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.repositorios.ConsultaRepository;
import br.com.sismedicina.gestor.repositorios.TecnicoRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConsultaService {
    @Autowired
    private ConsultaRepository consultaRepository;
    @Autowired
    private TecnicoRepositorio tecnicoRepositorio;

    public List<ConsultaDisponivelResponse> buscarAgendasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        List<Tecnico> tecnicos;
        if (filtro.getIdEspecialidade() == null) {
            tecnicos = tecnicoRepositorio.findAll();
        } else {
            tecnicos = tecnicoRepositorio.findAllByEspecialidade_Id(filtro.getIdEspecialidade());
        }

        Map<Long, Tecnico> tecnicoMap = new HashMap<>();
        for (Tecnico tecnico : tecnicos) {
            tecnicoMap.put(tecnico.getId(), tecnico);
        }

        List<Consulta> consultas = consultaRepository.findConsultasDisponiveis(filtro.getData(), tecnicoMap.keySet());


        List<ConsultaDisponivelResponse> responseList = new ArrayList<>();
        for (Consulta consulta : consultas) {
            if (tecnicoMap.containsKey(consulta.getTecnicoId())) {
                Tecnico tecnico = tecnicoMap.get(consulta.getTecnicoId());
                ConsultaDisponivelResponse response = new ConsultaDisponivelResponse();
                response.setDataMarcada(consulta.getDataMarcada());
                response.setDataIncio(consulta.getInicioHorario());
                response.setEspecialidade(tecnico.getEspecialidade());
                response.setIdTecnico(tecnico.getId());
                responseList.add(response);
            }
        }

        return responseList;
    }

}

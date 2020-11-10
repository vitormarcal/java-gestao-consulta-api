package br.com.sismedicina.gestor.controller;

import br.com.sismedicina.gestor.payload.request.FiltroConsultaDisponivelRequest;
import br.com.sismedicina.gestor.payload.response.ConsultaDisponivelResponse;
import br.com.sismedicina.gestor.services.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("consultas")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;


    @GetMapping("/disponiveis")
    public List<ConsultaDisponivelResponse> buscarConsultasDisponiveis(FiltroConsultaDisponivelRequest filtro) {
        return consultaService.buscarAgendasDisponiveis(filtro);
    }
}

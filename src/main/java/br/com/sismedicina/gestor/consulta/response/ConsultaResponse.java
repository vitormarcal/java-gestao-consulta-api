package br.com.sismedicina.gestor.consulta.response;

import br.com.sismedicina.gestor.auth.model.User;
import br.com.sismedicina.gestor.consulta.model.Consulta;
import br.com.sismedicina.gestor.tecnico.model.Tecnico;

import java.time.LocalDateTime;

public class ConsultaResponse {
    private Long idConsulta;
    private LocalDateTime dataConsulta;
    private LocalDateTime previsaoFim;
    private LocalDateTime fimConsulta;
    private String nomeTecnico;
    private Long idUsuarioTecnico;
    private Long idTecnico;
    private String nomeUsuario;
    private String usernameTecnico;
    private String usernameUsuario;
    private Long idUsuario;
    private String especialidade;

    public ConsultaResponse(Consulta consulta, Tecnico tecnico, User userTencnico, User user) {
        idConsulta = consulta.getId();
        dataConsulta = LocalDateTime.of(consulta.getDataMarcada(), consulta.getInicioHorario());
        if (consulta.getFimHorario() != null) {
            fimConsulta = LocalDateTime.of(consulta.getDataMarcada(), consulta.getFimHorario());
        }
        previsaoFim = dataConsulta.plusMinutes(tecnico.getDuracaoAtendimento());
        nomeTecnico = userTencnico.getNome();
        usernameTecnico = userTencnico.getUsername();
        usernameUsuario = user.getUsername();
        especialidade = tecnico.getEspecialidade().getDescricao();
        nomeUsuario = user.getNome();
        idUsuarioTecnico = userTencnico.getId();
        idTecnico = tecnico.getId();
        idUsuario = user.getId();

    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public LocalDateTime getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(LocalDateTime dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public String getNomeTecnico() {
        return nomeTecnico;
    }

    public void setNomeTecnico(String nomeTecnico) {
        this.nomeTecnico = nomeTecnico;
    }

    public Long getIdUsuarioTecnico() {
        return idUsuarioTecnico;
    }

    public void setIdUsuarioTecnico(Long idUsuarioTecnico) {
        this.idUsuarioTecnico = idUsuarioTecnico;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getUsernameTecnico() {
        return usernameTecnico;
    }

    public void setUsernameTecnico(String usernameTecnico) {
        this.usernameTecnico = usernameTecnico;
    }

    public String getUsernameUsuario() {
        return usernameUsuario;
    }

    public void setUsernameUsuario(String usernameUsuario) {
        this.usernameUsuario = usernameUsuario;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public LocalDateTime getPrevisaoFim() {
        return previsaoFim;
    }

    public void setPrevisaoFim(LocalDateTime previsaoFim) {
        this.previsaoFim = previsaoFim;
    }

    public LocalDateTime getFimConsulta() {
        return fimConsulta;
    }

    public void setFimConsulta(LocalDateTime fimConsulta) {
        this.fimConsulta = fimConsulta;
    }

    public Long getIdTecnico() {
        return idTecnico;
    }

    public void setIdTecnico(Long idTecnico) {
        this.idTecnico = idTecnico;
    }
}

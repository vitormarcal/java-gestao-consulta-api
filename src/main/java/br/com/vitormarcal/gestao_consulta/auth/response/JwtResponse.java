package br.com.vitormarcal.gestao_consulta.auth.response;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private Long id;
    private Long tecnicoId;
    private String username;
    private String email;
    private boolean cadastroCompleto;
    private List<String> roles;

    public JwtResponse(String accessToken, Long id, String username, String email,boolean cadastroCompleto, List<String> roles, Long tecnicoId) {
        this.token = accessToken;
        this.id = id;
        this.tecnicoId = tecnicoId;
        this.username = username;
        this.email = email;
        this.cadastroCompleto = cadastroCompleto;
        this.roles = roles;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public boolean isCadastroCompleto() {
        return cadastroCompleto;
    }

    public void setCadastroCompleto(boolean cadastroCompleto) {
        this.cadastroCompleto = cadastroCompleto;
    }

    public Long getTecnicoId() {
        return tecnicoId;
    }
}
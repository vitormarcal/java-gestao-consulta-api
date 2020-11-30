package br.com.vitormarcal.gestao_consulta.chat.request;

public class MessageRequest {
    String de;
    String para;
    String texto;
    Long idConsulta;
    Boolean exited;

    public String getDe() {
        return de;
    }

    public void setDe(String de) {
        this.de = de;
    }

    public String getPara() {
        return para;
    }

    public void setPara(String para) {
        this.para = para;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Long getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Long idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Boolean getExited() {
        return exited;
    }

    public void setExited(Boolean exited) {
        this.exited = exited;
    }
}

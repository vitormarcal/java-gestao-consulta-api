package br.com.vitormarcal.gestao_consulta.chat.response;

import br.com.vitormarcal.gestao_consulta.chat.model.Mensagem;
import br.com.vitormarcal.gestao_consulta.chat.request.MessageRequest;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;

public class OutputMessageResponse extends MessageRequest {
    private String time;

    public OutputMessageResponse(final String de, final String texto, final String time, final Boolean exited) {
        setTime(de);
        setTexto(texto);
        setExited(exited);
        this.time = time;
    }

    public OutputMessageResponse(Mensagem mensagem) {
        setTime(new SimpleDateFormat("HH:mm").format(Date.from(mensagem.getEm().atZone(ZoneId.systemDefault()).toInstant())));
        setTexto(mensagem.getTexto());
        setPara(mensagem.getPara());
        setDe(mensagem.getDe());
        setIdConsulta(mensagem.getConsultaId());
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

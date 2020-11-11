package br.com.sismedicina.gestor.payload.response;

import br.com.sismedicina.gestor.payload.request.MessageRequest;

public class OutputMessageResponse extends MessageRequest {
    private String time;

    public OutputMessageResponse(final String de, final String texto, final String time) {
        setTime(de);
        setTexto(texto);
        this.time = time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

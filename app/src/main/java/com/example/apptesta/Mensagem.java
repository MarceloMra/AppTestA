package com.example.apptesta;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Mensagem {
    private String mensagem;
    private String hora;
    private String endpointID;
    private String modo;
    private SimpleDateFormat sdf;

    public Mensagem(String mensagem, String modo, String endpointID){
        sdf = new SimpleDateFormat("HH:mm");
        Date hora = Calendar.getInstance().getTime();
        this.mensagem = mensagem;
        this.modo = modo;
        this.endpointID = endpointID;
        this.hora = sdf.format(hora);
    }

    public String getEndpointID() {
        return endpointID;
    }

    public void setEndpointID(String endpointID) {
        this.endpointID = endpointID;
    }

    public String getModo() {
        return modo;
    }

    public void setModo(String modo) {
        this.modo = modo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getHora() {
        return hora;
    }

    public void setData(String data) {
        this.hora = data;
    }


}

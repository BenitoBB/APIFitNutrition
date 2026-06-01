/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author julia
 */
public class Respuesta {
    private boolean error;
    private String mensaje;
    private Object valor;

    public Respuesta() {
    }

    public Respuesta(boolean error, String mensaje) {
        this.error = error;
        this.mensaje = mensaje;
    }

    public Respuesta(boolean error, String mensaje, Object valor) {
        this.error = error;
        this.mensaje = mensaje;
        this.valor = valor;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Object getValor() {
        return valor;
    }

    public void setValor(Object valor) {
        this.valor = valor;
    }
}

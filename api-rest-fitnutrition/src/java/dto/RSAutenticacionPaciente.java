/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import pojo.Paciente;

/**
 *
 * @author julia
 */
public class RSAutenticacionPaciente {
    String mensaje;
    boolean error;
    Paciente paciente;

    public RSAutenticacionPaciente() {
    }

    public RSAutenticacionPaciente(String mensaje, boolean error, Paciente paciente) {
        this.mensaje = mensaje;
        this.error = error;
        this.paciente = paciente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }
}

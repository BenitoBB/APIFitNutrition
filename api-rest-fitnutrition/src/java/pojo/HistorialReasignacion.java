/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 *
 * @author julia
 */
public class HistorialReasignacion {
    private int idReasignacion;
    private int idPaciente;
    private int idMedicoAnterior;
    private int idMedicoNuevo;
    private String fechaReasignacion; // Mapeado como String
    private String motivo;

    public HistorialReasignacion(int idReasignacion, int idPaciente, int idMedicoAnterior, int idMedicoNuevo, String fechaReasignacion, String motivo) {
        this.idReasignacion = idReasignacion;
        this.idPaciente = idPaciente;
        this.idMedicoAnterior = idMedicoAnterior;
        this.idMedicoNuevo = idMedicoNuevo;
        this.fechaReasignacion = fechaReasignacion;
        this.motivo = motivo;
    }

    public int getIdReasignacion() {
        return idReasignacion;
    }

    public void setIdReasignacion(int idReasignacion) {
        this.idReasignacion = idReasignacion;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedicoAnterior() {
        return idMedicoAnterior;
    }

    public void setIdMedicoAnterior(int idMedicoAnterior) {
        this.idMedicoAnterior = idMedicoAnterior;
    }

    public int getIdMedicoNuevo() {
        return idMedicoNuevo;
    }

    public void setIdMedicoNuevo(int idMedicoNuevo) {
        this.idMedicoNuevo = idMedicoNuevo;
    }

    public String getFechaReasignacion() {
        return fechaReasignacion;
    }

    public void setFechaReasignacion(String fechaReasignacion) {
        this.fechaReasignacion = fechaReasignacion;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
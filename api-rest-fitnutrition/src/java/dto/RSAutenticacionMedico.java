package dto;

import pojo.Medico;

public class RSAutenticacionMedico extends Respuesta {
    private Medico medico;
    private boolean esAdministrador;

    public RSAutenticacionMedico() {
    }

    public RSAutenticacionMedico(boolean error, String mensaje, Medico medico, boolean esAdministrador) {
        super(error, mensaje);
        this.medico = medico;
        this.esAdministrador = esAdministrador;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public boolean isEsAdministrador() {
        return esAdministrador;
    }

    public void setEsAdministrador(boolean esAdministrador) {
        this.esAdministrador = esAdministrador;
    }
}

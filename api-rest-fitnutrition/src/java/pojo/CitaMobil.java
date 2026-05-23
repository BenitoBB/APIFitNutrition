package pojo;

/**
 * Versión de Cita para la app móvil. NO incluye el campo 'observaciones' por la
 * regla RN-19 (las observaciones son privadas y solo visibles para el médico en
 * escritorio).
 */
public class CitaMobil {

    private int idCita;
    private String fechaCita;
    private String horaCita;
    private int idPaciente;
    private int idMedico;
    private String estatus;
    private String motivoCancelacion;
    private String fechaCreacion;

    // Datos del médico para mostrar en la app
    private String medicoNombre;
    private String medicoPrimerApellido;
    private String medicoSegundoApellido;

    public CitaMobil() {
    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public String getFechaCita() {
        return fechaCita;
    }

    public void setFechaCita(String fechaCita) {
        this.fechaCita = fechaCita;
    }

    public String getHoraCita() {
        return horaCita;
    }

    public void setHoraCita(String horaCita) {
        this.horaCita = horaCita;
    }

    public int getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(int idPaciente) {
        this.idPaciente = idPaciente;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }

    public void setMotivoCancelacion(String motivoCancelacion) {
        this.motivoCancelacion = motivoCancelacion;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getMedicoNombre() {
        return medicoNombre;
    }

    public void setMedicoNombre(String medicoNombre) {
        this.medicoNombre = medicoNombre;
    }

    public String getMedicoPrimerApellido() {
        return medicoPrimerApellido;
    }

    public void setMedicoPrimerApellido(String medicoPrimerApellido) {
        this.medicoPrimerApellido = medicoPrimerApellido;
    }

    public String getMedicoSegundoApellido() {
        return medicoSegundoApellido;
    }

    public void setMedicoSegundoApellido(String medicoSegundoApellido) {
        this.medicoSegundoApellido = medicoSegundoApellido;
    }
}

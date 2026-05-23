package pojo;

public class CitaDetalle extends Cita {
    private String pacienteNombre;
    private String pacientePrimerApellido;
    private String pacienteSegundoApellido;
    private String pacienteEmail;
    
    private String medicoNombre;
    private String medicoPrimerApellido;
    private String medicoSegundoApellido;

    public CitaDetalle() {
        super();
    }

    public String getPacienteNombre() {
        return pacienteNombre;
    }

    public void setPacienteNombre(String pacienteNombre) {
        this.pacienteNombre = pacienteNombre;
    }

    public String getPacientePrimerApellido() {
        return pacientePrimerApellido;
    }

    public void setPacientePrimerApellido(String pacientePrimerApellido) {
        this.pacientePrimerApellido = pacientePrimerApellido;
    }

    public String getPacienteSegundoApellido() {
        return pacienteSegundoApellido;
    }

    public void setPacienteSegundoApellido(String pacienteSegundoApellido) {
        this.pacienteSegundoApellido = pacienteSegundoApellido;
    }

    public String getPacienteEmail() {
        return pacienteEmail;
    }

    public void setPacienteEmail(String pacienteEmail) {
        this.pacienteEmail = pacienteEmail;
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

package dto;

public class PacienteUpdateRequest {

    // ── Campos de paciente ──────────────────────────────────────────────────
    private int    idPaciente;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String fechaNacimiento;
    private String sexo;
    private String email;
    private String telefono;        // Opcional; si viene, debe tener 10 dígitos
    private Integer idDomicilio;
    private String codigoAcceso;    // Opcional; si viene, se hashea
    private int    idMedico;
    private int    estatus;

    // ── Campos de domicilio (todos opcionales) ──────────────────────────────
    // Si al menos uno llega con valor, se opera sobre la tabla domicilio.
    // Si ninguno llega, la tabla domicilio NO se toca.
    private String  calle;
    private String  numero;
    private Integer idColonia;

    // ── Getters / Setters ───────────────────────────────────────────────────

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getIdDomicilio() { return idDomicilio; }
    public void setIdDomicilio(Integer idDomicilio) { this.idDomicilio = idDomicilio; }

    public String getCodigoAcceso() { return codigoAcceso; }
    public void setCodigoAcceso(String codigoAcceso) { this.codigoAcceso = codigoAcceso; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public int getEstatus() { return estatus; }
    public void setEstatus(int estatus) { this.estatus = estatus; }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public Integer getIdColonia() { return idColonia; }
    public void setIdColonia(Integer idColonia) { this.idColonia = idColonia; }

    /**
     * Retorna true si el cliente envió datos de domicilio completos.
     * Solo en ese caso se opera sobre la tabla domicilio.
     */
    public boolean tieneDomicilio() {
        return calle  != null && !calle.trim().isEmpty()
            && numero != null && !numero.trim().isEmpty()
            && idColonia != null;
    }
}
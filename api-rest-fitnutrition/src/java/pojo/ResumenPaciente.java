package pojo;

public class ResumenPaciente {
    private int idPaciente;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String medico;
    private double pesoActual;
    private double talla;
    private double imcActual;
    private String clasificacionImc; // Bajo peso / Normal / Sobrepeso / Obesidad

    public ResumenPaciente() {}

    public int getIdPaciente() { return idPaciente; }
    public void setIdPaciente(int idPaciente) { this.idPaciente = idPaciente; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }

    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }

    public String getMedico() { return medico; }
    public void setMedico(String medico) { this.medico = medico; }

    public double getPesoActual() { return pesoActual; }
    public void setPesoActual(double pesoActual) { this.pesoActual = pesoActual; }

    public double getTalla() { return talla; }
    public void setTalla(double talla) { this.talla = talla; }

    public double getImcActual() { return imcActual; }
    public void setImcActual(double imcActual) { this.imcActual = imcActual; }

    public String getClasificacionImc() { return clasificacionImc; }
    public void setClasificacionImc(String clasificacionImc) { this.clasificacionImc = clasificacionImc; }
}
package pojo;

public class UltimaConsulta {
    private int idConsulta;
    private String fechaConsulta;
    private double peso;
    private double talla;
    private double imc;
    private String clasificacionImc;
    private String nombreDieta;
    private String observaciones;

    public UltimaConsulta() {}

    public int getIdConsulta() { return idConsulta; }
    public void setIdConsulta(int idConsulta) { this.idConsulta = idConsulta; }

    public String getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(String fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getTalla() { return talla; }
    public void setTalla(double talla) { this.talla = talla; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getClasificacionImc() { return clasificacionImc; }
    public void setClasificacionImc(String clasificacionImc) { this.clasificacionImc = clasificacionImc; }

    public String getNombreDieta() { return nombreDieta; }
    public void setNombreDieta(String nombreDieta) { this.nombreDieta = nombreDieta; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
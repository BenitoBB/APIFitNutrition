package pojo;

public class PuntoPeso {
    private String fechaConsulta;
    private double peso;
    private double imc;
    private String clasificacionImc;

    public PuntoPeso() {}

    public String getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(String fechaConsulta) { this.fechaConsulta = fechaConsulta; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public double getImc() { return imc; }
    public void setImc(double imc) { this.imc = imc; }

    public String getClasificacionImc() { return clasificacionImc; }
    public void setClasificacionImc(String clasificacionImc) { this.clasificacionImc = clasificacionImc; }
}
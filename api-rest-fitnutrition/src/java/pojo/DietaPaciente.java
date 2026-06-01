package pojo;

import java.util.List;

public class DietaPaciente {
    private int idDieta;
    private String nombreDieta;
    private double totalCalorias;
    private String observaciones;
    private int idMedico;
    private String medico;
    private List<CategoriaHorarioDieta> categorias;
    private boolean esActual;


    public DietaPaciente() {}

    public int getIdDieta() { return idDieta; }
    public void setIdDieta(int idDieta) { this.idDieta = idDieta; }

    public String getNombreDieta() { return nombreDieta; }
    public void setNombreDieta(String nombreDieta) { this.nombreDieta = nombreDieta; }

    public double getTotalCalorias() { return totalCalorias; }
    public void setTotalCalorias(double totalCalorias) { this.totalCalorias = totalCalorias; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public int getIdMedico() { return idMedico; }
    public void setIdMedico(int idMedico) { this.idMedico = idMedico; }

    public String getMedico() { return medico; }
    public void setMedico(String medico) { this.medico = medico; }

    public List<CategoriaHorarioDieta> getCategorias() { return categorias; }
    public void setCategorias(List<CategoriaHorarioDieta> categorias) { this.categorias = categorias; }
    
    public boolean isEsActual() { return esActual; }
    public void setEsActual(boolean esActual) { this.esActual = esActual; }
}
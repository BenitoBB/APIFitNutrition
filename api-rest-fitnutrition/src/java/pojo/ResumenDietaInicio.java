package pojo;

import java.util.List;

public class ResumenDietaInicio {
    private String nombreDieta;
    private double totalCalorias;
    private List<CaloriasPorCategoria> categorias;

    public ResumenDietaInicio() {}

    public String getNombreDieta() { return nombreDieta; }
    public void setNombreDieta(String nombreDieta) { this.nombreDieta = nombreDieta; }

    public double getTotalCalorias() { return totalCalorias; }
    public void setTotalCalorias(double totalCalorias) { this.totalCalorias = totalCalorias; }

    public List<CaloriasPorCategoria> getCategorias() { return categorias; }
    public void setCategorias(List<CaloriasPorCategoria> categorias) { this.categorias = categorias; }
}
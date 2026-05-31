package pojo;

import java.util.List;

public class CategoriaHorarioDieta {
    private int idCategoria;
    private String nombreCategoria;
    private List<AlimentoDieta> alimentos;

    public CategoriaHorarioDieta() {}

    public int getIdCategoria() { return idCategoria; }
    public void setIdCategoria(int idCategoria) { this.idCategoria = idCategoria; }

    public String getNombreCategoria() { return nombreCategoria; }
    public void setNombreCategoria(String nombreCategoria) { this.nombreCategoria = nombreCategoria; }

    public List<AlimentoDieta> getAlimentos() { return alimentos; }
    public void setAlimentos(List<AlimentoDieta> alimentos) { this.alimentos = alimentos; }
}
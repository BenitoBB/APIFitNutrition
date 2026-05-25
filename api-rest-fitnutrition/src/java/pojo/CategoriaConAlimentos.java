/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

import java.util.List;

/**
 * Categoría de horario con los alimentos que le pertenecen dentro de una dieta.
 * T318: componente del detalle GET /api/dieta/{id}
 */
public class CategoriaConAlimentos {
    private int idCategoria;
    private String nombreCategoria;
    private List<AlimentoEnDieta> alimentos;

    public CategoriaConAlimentos() {
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }

    public List<AlimentoEnDieta> getAlimentos() {
        return alimentos;
    }

    public void setAlimentos(List<AlimentoEnDieta> alimentos) {
        this.alimentos = alimentos;
    }
}

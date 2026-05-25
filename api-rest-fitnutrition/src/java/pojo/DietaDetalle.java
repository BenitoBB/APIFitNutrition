/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

import java.util.List;

/**
 * Detalle completo de una dieta con sus categorías y los alimentos de cada categoría.
 * T318: respuesta de GET /api/dieta/{id}
 * El campo editable proviene de la vista vw_dieta_pacientes_count (1=sí, 0=no).
 */
public class DietaDetalle {
    private int idDieta;
    private String nombreDieta;
    private double totalCalorias;
    private String observaciones;
    private int idMedico;
    private int editable;
    private List<CategoriaConAlimentos> categorias;

    public DietaDetalle() {
    }

    public int getIdDieta() {
        return idDieta;
    }

    public void setIdDieta(int idDieta) {
        this.idDieta = idDieta;
    }

    public String getNombreDieta() {
        return nombreDieta;
    }

    public void setNombreDieta(String nombreDieta) {
        this.nombreDieta = nombreDieta;
    }

    public double getTotalCalorias() {
        return totalCalorias;
    }

    public void setTotalCalorias(double totalCalorias) {
        this.totalCalorias = totalCalorias;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getIdMedico() {
        return idMedico;
    }

    public void setIdMedico(int idMedico) {
        this.idMedico = idMedico;
    }

    public int getEditable() {
        return editable;
    }

    public void setEditable(int editable) {
        this.editable = editable;
    }

    public List<CategoriaConAlimentos> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<CategoriaConAlimentos> categorias) {
        this.categorias = categorias;
    }
}

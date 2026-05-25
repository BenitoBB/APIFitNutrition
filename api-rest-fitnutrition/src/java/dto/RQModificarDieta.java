/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;

/**
 * DTO de entrada para modificar una dieta existente.
 * T319: PUT /api/dieta/modificar
 * SP-3 valida RN-13 antes de cualquier cambio.
 * Cada campo es opcional excepto idDieta; se aplican solo los que vienen en el request.
 */
public class RQModificarDieta {
    private int idDieta;
    private String nombreDieta;
    private String observaciones;
    private List<String> categoriasAgregar;
    private List<Integer> categoriasEliminar;
    private List<RQAlimentoEnCategoria> alimentosAgregar;
    private List<RQAlimentoEnCategoria> alimentosEliminar;

    public RQModificarDieta() {
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<String> getCategoriasAgregar() {
        return categoriasAgregar;
    }

    public void setCategoriasAgregar(List<String> categoriasAgregar) {
        this.categoriasAgregar = categoriasAgregar;
    }

    public List<Integer> getCategoriasEliminar() {
        return categoriasEliminar;
    }

    public void setCategoriasEliminar(List<Integer> categoriasEliminar) {
        this.categoriasEliminar = categoriasEliminar;
    }

    public List<RQAlimentoEnCategoria> getAlimentosAgregar() {
        return alimentosAgregar;
    }

    public void setAlimentosAgregar(List<RQAlimentoEnCategoria> alimentosAgregar) {
        this.alimentosAgregar = alimentosAgregar;
    }

    public List<RQAlimentoEnCategoria> getAlimentosEliminar() {
        return alimentosEliminar;
    }

    public void setAlimentosEliminar(List<RQAlimentoEnCategoria> alimentosEliminar) {
        this.alimentosEliminar = alimentosEliminar;
    }
}

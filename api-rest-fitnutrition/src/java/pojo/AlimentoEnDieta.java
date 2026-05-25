/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

/**
 * Alimento con su cantidad asignada dentro de una categoría de horario de una dieta.
 * T318: usado en el detalle GET /api/dieta/{id}
 */
public class AlimentoEnDieta {
    private int idAlimento;
    private String nombreAlimento;
    private String porcion;
    private double caloriasPorcion;
    private double cantidad;

    public AlimentoEnDieta() {
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public String getNombreAlimento() {
        return nombreAlimento;
    }

    public void setNombreAlimento(String nombreAlimento) {
        this.nombreAlimento = nombreAlimento;
    }

    public String getPorcion() {
        return porcion;
    }

    public void setPorcion(String porcion) {
        this.porcion = porcion;
    }

    public double getCaloriasPorcion() {
        return caloriasPorcion;
    }

    public void setCaloriasPorcion(double caloriasPorcion) {
        this.caloriasPorcion = caloriasPorcion;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}

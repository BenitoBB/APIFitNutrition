/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 * Item que representa un alimento dentro de una categoría de horario,
 * usado en el request de modificación de dieta (agregar o eliminar).
 * T319: cantidad solo se usa al agregar; se ignora al eliminar.
 */
public class RQAlimentoEnCategoria {
    private int idCategoria;
    private int idAlimento;
    private double cantidad;

    public RQAlimentoEnCategoria() {
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdAlimento() {
        return idAlimento;
    }

    public void setIdAlimento(int idAlimento) {
        this.idAlimento = idAlimento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }
}

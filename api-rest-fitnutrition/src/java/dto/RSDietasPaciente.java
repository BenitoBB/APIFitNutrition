/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

import java.util.List;
import pojo.DietaPaciente;

/**
 *
 * @author julia
 */
public class RSDietasPaciente {
    private DietaPaciente dietaActual;
    private List<DietaPaciente> historial;

    public RSDietasPaciente() {
    }

    public RSDietasPaciente(DietaPaciente dietaActual, List<DietaPaciente> historial) {
        this.dietaActual = dietaActual;
        this.historial = historial;
    }

    public DietaPaciente getDietaActual() {
        return dietaActual;
    }

    public void setDietaActual(DietaPaciente dietaActual) {
        this.dietaActual = dietaActual;
    }

    public List<DietaPaciente> getHistorial() {
        return historial;
    }

    public void setHistorial(List<DietaPaciente> historial) {
        this.historial = historial;
    }
}

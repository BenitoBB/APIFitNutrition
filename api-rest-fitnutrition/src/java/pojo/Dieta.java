/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pojo;

public class Dieta {
    private int idDieta;
    private String nombreDieta;
    private double totalCalorias;
    private String observaciones;
    private int idMedico;

    public Dieta(int idDieta, String nombreDieta, double totalCalorias, String observaciones, int idMedico) {
        this.idDieta = idDieta;
        this.nombreDieta = nombreDieta;
        this.totalCalorias = totalCalorias;
        this.observaciones = observaciones;
        this.idMedico = idMedico;
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
}
package com.example.adrentar.dto;

import java.util.Date;

public class EditarAlquilerDto {

    private Double precio;
    private Date fechaInicio;
    private Date fechaFin;
    private Double porcentajeAumento;

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }
    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }
    public Double getPorcentajeAumento() { return porcentajeAumento; }
    public void setPorcentajeAumento(Double porcentajeAumento) { this.porcentajeAumento = porcentajeAumento; }
}


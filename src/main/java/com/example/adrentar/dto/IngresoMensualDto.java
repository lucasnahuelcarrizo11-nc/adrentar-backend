package com.example.adrentar.dto;

public class IngresoMensualDto {

    private Integer mes;
    private Double total;

    public IngresoMensualDto(Integer mes, Double total) {
        this.mes = mes;
        this.total = total;
    }

    public Integer getMes() { return mes; }
    public void setMes(Integer mes) { this.mes = mes; }
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
}
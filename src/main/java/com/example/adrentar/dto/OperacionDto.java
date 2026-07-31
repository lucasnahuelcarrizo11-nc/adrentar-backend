package com.example.adrentar.dto;

public class OperacionDto {

    private Long idAlquiler;
    private Integer anio;
    private String mes;
    private String propiedad;
    private String mailInquilino;
    private Double precioTotal;
    private String estado;
    private Long pagosRealizados;
    private Long pagosTotales;
    private Long reclamos;
    private String proximaRenovacion;

    public OperacionDto(Long idAlquiler, Integer anio, String mes, String propiedad, String mailInquilino,
                        Double precioTotal, String estado, Long pagosRealizados, Long pagosTotales,
                        Long reclamos, String proximaRenovacion) {
        this.idAlquiler = idAlquiler;
        this.anio = anio;
        this.mes = mes;
        this.propiedad = propiedad;
        this.mailInquilino = mailInquilino;
        this.precioTotal = precioTotal;
        this.estado = estado;
        this.pagosRealizados = pagosRealizados;
        this.pagosTotales = pagosTotales;
        this.reclamos = reclamos;
        this.proximaRenovacion = proximaRenovacion;
    }

    // getters y setters
    public Long getIdAlquiler() { return idAlquiler; }
    public void setIdAlquiler(Long idAlquiler) { this.idAlquiler = idAlquiler; }
    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }
    public String getMes() { return mes; }
    public void setMes(String mes) { this.mes = mes; }
    public String getPropiedad() { return propiedad; }
    public void setPropiedad(String propiedad) { this.propiedad = propiedad; }
    public String getMailInquilino() { return mailInquilino; }
    public void setMailInquilino(String mailInquilino) { this.mailInquilino = mailInquilino; }
    public Double getPrecioTotal() { return precioTotal; }
    public void setPrecioTotal(Double precioTotal) { this.precioTotal = precioTotal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Long getPagosRealizados() { return pagosRealizados; }
    public void setPagosRealizados(Long pagosRealizados) { this.pagosRealizados = pagosRealizados; }
    public Long getPagosTotales() { return pagosTotales; }
    public void setPagosTotales(Long pagosTotales) { this.pagosTotales = pagosTotales; }
    public Long getReclamos() { return reclamos; }
    public void setReclamos(Long reclamos) { this.reclamos = reclamos; }
    public String getProximaRenovacion() { return proximaRenovacion; }
    public void setProximaRenovacion(String proximaRenovacion) { this.proximaRenovacion = proximaRenovacion; }
}
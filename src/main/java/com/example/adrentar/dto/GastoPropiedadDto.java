package com.example.adrentar.dto;

public class GastoPropiedadDto {


    private Long idPropiedad;
    private String tituloPropiedad;
    private Long cantidadReparaciones;
    private Double gastoTotal;


    public GastoPropiedadDto(Long idPropiedad, String tituloPropiedad, Long cantidadReparaciones, Double gastoTotal) {
        this.idPropiedad = idPropiedad;
        this.tituloPropiedad = tituloPropiedad;
        this.cantidadReparaciones = cantidadReparaciones;
        this.gastoTotal = gastoTotal;
    }

    // getters y setters
    public Long getIdPropiedad() { return idPropiedad; }
    public void setIdPropiedad(Long idPropiedad) { this.idPropiedad = idPropiedad; }

    public String getTituloPropiedad() { return tituloPropiedad; }
    public void setTituloPropiedad(String tituloPropiedad) { this.tituloPropiedad = tituloPropiedad; }

    public Long getCantidadReparaciones() { return cantidadReparaciones; }
    public void setCantidadReparaciones(Long cantidadReparaciones) { this.cantidadReparaciones = cantidadReparaciones; }

    public Double getGastoTotal() { return gastoTotal; }
    public void setGastoTotal(Double gastoTotal) { this.gastoTotal = gastoTotal; }
}

package com.example.adrentar.dto;

public class ResumenInquilinoDto {

    private Long idAlquiler;
    private Integer progresoContratoPorcentaje;
    private Integer diasRestantesContrato;
    private String proximoAumentoFecha;
    private Integer diasParaProximoAumento;

    private Double totalPagado;
    private Long pagosAprobados;
    private Long pagosPendientes;
    private Long pagosRechazados;

    private Long reclamosPendientes;
    private Long reclamosEnRevision;
    private Long reclamosResueltos;

    public ResumenInquilinoDto(Long idAlquiler, Integer progresoContratoPorcentaje, Integer diasRestantesContrato,
                               String proximoAumentoFecha, Integer diasParaProximoAumento, Double totalPagado,
                               Long pagosAprobados, Long pagosPendientes, Long pagosRechazados,
                               Long reclamosPendientes, Long reclamosEnRevision, Long reclamosResueltos) {
        this.idAlquiler = idAlquiler;
        this.progresoContratoPorcentaje = progresoContratoPorcentaje;
        this.diasRestantesContrato = diasRestantesContrato;
        this.proximoAumentoFecha = proximoAumentoFecha;
        this.diasParaProximoAumento = diasParaProximoAumento;
        this.totalPagado = totalPagado;
        this.pagosAprobados = pagosAprobados;
        this.pagosPendientes = pagosPendientes;
        this.pagosRechazados = pagosRechazados;
        this.reclamosPendientes = reclamosPendientes;
        this.reclamosEnRevision = reclamosEnRevision;
        this.reclamosResueltos = reclamosResueltos;
    }

    public Long getIdAlquiler() { return idAlquiler; }
    public void setIdAlquiler(Long idAlquiler) { this.idAlquiler = idAlquiler; }
    public Integer getProgresoContratoPorcentaje() { return progresoContratoPorcentaje; }
    public void setProgresoContratoPorcentaje(Integer progresoContratoPorcentaje) { this.progresoContratoPorcentaje = progresoContratoPorcentaje; }
    public Integer getDiasRestantesContrato() { return diasRestantesContrato; }
    public void setDiasRestantesContrato(Integer diasRestantesContrato) { this.diasRestantesContrato = diasRestantesContrato; }
    public String getProximoAumentoFecha() { return proximoAumentoFecha; }
    public void setProximoAumentoFecha(String proximoAumentoFecha) { this.proximoAumentoFecha = proximoAumentoFecha; }
    public Integer getDiasParaProximoAumento() { return diasParaProximoAumento; }
    public void setDiasParaProximoAumento(Integer diasParaProximoAumento) { this.diasParaProximoAumento = diasParaProximoAumento; }
    public Double getTotalPagado() { return totalPagado; }
    public void setTotalPagado(Double totalPagado) { this.totalPagado = totalPagado; }
    public Long getPagosAprobados() { return pagosAprobados; }
    public void setPagosAprobados(Long pagosAprobados) { this.pagosAprobados = pagosAprobados; }
    public Long getPagosPendientes() { return pagosPendientes; }
    public void setPagosPendientes(Long pagosPendientes) { this.pagosPendientes = pagosPendientes; }
    public Long getPagosRechazados() { return pagosRechazados; }
    public void setPagosRechazados(Long pagosRechazados) { this.pagosRechazados = pagosRechazados; }
    public Long getReclamosPendientes() { return reclamosPendientes; }
    public void setReclamosPendientes(Long reclamosPendientes) { this.reclamosPendientes = reclamosPendientes; }
    public Long getReclamosEnRevision() { return reclamosEnRevision; }
    public void setReclamosEnRevision(Long reclamosEnRevision) { this.reclamosEnRevision = reclamosEnRevision; }
    public Long getReclamosResueltos() { return reclamosResueltos; }
    public void setReclamosResueltos(Long reclamosResueltos) { this.reclamosResueltos = reclamosResueltos; }
}
package es.udc.ws.app.client.service.dto;

import java.time.LocalDateTime;

public class ClientExcursionDto {

    private Long excId;
    private String ciudad;
    private String descripcion;
    private LocalDateTime fechaCelebracion;
    private Float precio;
    private short numMaxPers;
    private short numPers;

    public ClientExcursionDto(){};

    public ClientExcursionDto(Long excId, String ciudad, String descripcion, LocalDateTime fechaCelebracion, Float precio, short numMaxPers, short numPers) {
        this.excId = excId;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.fechaCelebracion = fechaCelebracion;
        this.precio = precio;
        this.numMaxPers = numMaxPers;
        this.numPers = numPers;
    }

    public Long getExcId() {
        return excId;
    }

    public void setExcId(Long excId) {
        this.excId = excId;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaCelebracion() {
        return fechaCelebracion;
    }

    public void setFechaCelebracion(LocalDateTime fechaCelebracion) {
        this.fechaCelebracion = fechaCelebracion;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public short getNumMaxPers() {return numMaxPers;}

    public void setNumMaxPers(short numMaxPers) {this.numMaxPers = numMaxPers;}

    public short getNumPers() {return numPers;}

    public void setNumPers(short numPers) {this.numPers = numPers;}

    @Override
    public String toString() {
        return "Excursion:" +
                "\n\tId=" + excId +
                "\n\tCiudad ='" + ciudad + '\'' +
                "\n\tDescripcion ='" + descripcion + '\'' +
                "\n\tFecha de celebracion=" + fechaCelebracion +
                "\n\tPrecio =" + precio +
                "\n\tPlazas ocupadas =" + numPers;
    }


}

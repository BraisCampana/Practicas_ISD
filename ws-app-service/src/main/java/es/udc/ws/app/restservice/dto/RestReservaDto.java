package es.udc.ws.app.restservice.dto;

import java.time.LocalDateTime;

public class RestReservaDto {

    private Long reservaId;
    private String email;
    private Long excId;
    private short numPlazas;
    private String last4digTarjeta;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaCancelacion;
    private double precioCompra;

    public RestReservaDto() {
    }

    public RestReservaDto(Long reservaId, String email, Long excId, short numPlazas, String last4digTarjeta, LocalDateTime fechaCreacion, LocalDateTime fechaCancelacion, double precioCompra) {
        this.reservaId = reservaId;
        this.email = email;
        this.excId = excId;
        this.numPlazas = numPlazas;
        this.last4digTarjeta = last4digTarjeta;
        this.fechaCreacion = fechaCreacion;
        this.fechaCancelacion = fechaCancelacion;
        this.precioCompra = precioCompra;
    }

    public Long getReservaId() {return reservaId;}

    public void setReservaId(Long reservaId) {this.reservaId = reservaId;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Long getExcId() {return excId;}

    public void setExcId(Long excId) {this.excId = excId;}

    public short getNumPlazas() {return numPlazas;}

    public void setNumPlazas(short numPlazas) {this.numPlazas = numPlazas;}

    public String getLast4digTarjeta() {return last4digTarjeta;}

    public void setLast4digTarjeta(String last4digTarjeta) {this.last4digTarjeta = last4digTarjeta;}

    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public void setFechaCreacion(LocalDateTime fechaCreacion) {this.fechaCreacion = fechaCreacion;}

    public LocalDateTime getFechaCancelacion() {return fechaCancelacion;}

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {this.fechaCancelacion = fechaCancelacion;}

    public double getPrecioCompra() {return precioCompra;}

    public void setPrecioCompra(double precioCompra) {this.precioCompra = precioCompra;}


}

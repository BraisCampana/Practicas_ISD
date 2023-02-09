package es.udc.ws.app.model.excursion;

import java.time.LocalDateTime;
import java.util.Objects;

public class Excursion {
    private Long excId;
    private String ciudad;
    private String descripcion;
    private LocalDateTime fechaCelebracion;
    private Float precio;
    private short numMaxPers;
    private short numPers;
    private LocalDateTime fechaCreacion;

    public Excursion(String ciudad, String descripcion, LocalDateTime fechaCelebracion, Float precio, short numMaxPers, short numPers) {
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.fechaCelebracion = fechaCelebracion;
        this.precio = precio;
        this.numMaxPers = numMaxPers;
        this.numPers = numPers;
    }

    public Excursion(Long excId, String ciudad, String descripcion, LocalDateTime fechaCelebracion, Float precio, short numMaxPers, short numPers) {
        this(ciudad, descripcion, fechaCelebracion, precio, numMaxPers, numPers);
        this.excId = excId;
    }

    public Excursion(Long excId, String ciudad, String descripcion, LocalDateTime fechaCelebracion, Float precio, short numMaxPers, short numPers, LocalDateTime fechaCreacion) {
        this(excId, ciudad, descripcion, fechaCelebracion, precio, numMaxPers, numPers);
        this.fechaCreacion = fechaCreacion;
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

    public short getNumMaxPers() {
        return numMaxPers;
    }

    public void setNumMaxPers(short numMaxPers) {
        this.numMaxPers = numMaxPers;
    }

    public short getNumPers() {
        return numPers;
    }

    public void setNumPers(short numPers) {
        this.numPers = numPers;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public short getNumPlazasDisponibles(){
        return (short)(numMaxPers - numPers);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Excursion excursion = (Excursion) o;
        return numMaxPers == excursion.numMaxPers && numPers == excursion.numPers && Objects.equals(excId, excursion.excId) && Objects.equals(ciudad, excursion.ciudad) && Objects.equals(descripcion, excursion.descripcion) && Objects.equals(fechaCelebracion, excursion.fechaCelebracion) && Objects.equals(precio, excursion.precio) && Objects.equals(fechaCreacion, excursion.fechaCreacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(excId, ciudad, descripcion, fechaCelebracion, precio, numMaxPers, numPers, fechaCreacion);
    }
}

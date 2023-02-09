package es.udc.ws.app.restservice.dto;

public class RestExcursionDto {

    private Long excId;
    private String ciudad;
    private String descripcion;
    private String fechaCelebracion;
    private Float precio;
    private short numMaxPers;
    private short numPers;

    RestExcursionDto(){}

    public RestExcursionDto(Long excId, String ciudad, String descripcion, String fechaCelebracion, Float precio, short numMaxPers) {
        this.excId = excId;
        this.ciudad = ciudad;
        this.descripcion = descripcion;
        this.fechaCelebracion = fechaCelebracion;
        this.precio = precio;
        this.numMaxPers = numMaxPers;
        this.numPers = 0;
    }

    public RestExcursionDto(Long excId, String ciudad, String descripcion, String fechaCelebracion, Float precio, short numMaxPers, short numPers) {
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

    public String getFechaCelebracion() {
        return fechaCelebracion;
    }

    public void setFechaCelebracion(String fechaCelebracion) {
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

    @Override
    public String toString() {
        return "ExcursionDto[" +
                "excId=" + excId +
                ", ciudad='" + ciudad + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", fechaCelebracion=" + fechaCelebracion +
                ", precio=" + precio +
                ", numMaxPers=" + numMaxPers +
                ", numPers=" + numPers +
                ']';
    }


}

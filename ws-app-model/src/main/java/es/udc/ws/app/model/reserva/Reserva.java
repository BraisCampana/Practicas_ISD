package es.udc.ws.app.model.reserva;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reserva {
    private Long reservaId;
    private String email;
    private Long excId;
    private short numPlazas;
    private String tarjetaCredito;
    private LocalDateTime fecha;
    private LocalDateTime fechaCancelacion;
    private float precioCompra;


    public Reserva(String email, Long excId, short numPlazas, String tarjetaCredito, float precioCompra) {
        this.email = email;
        this.excId = excId;
        this.numPlazas = numPlazas;
        this.tarjetaCredito = tarjetaCredito;
        this.fechaCancelacion = null;
        this.precioCompra = precioCompra;
    }

    //Constructor usado para indicar solamente os parametros para cancelar unha reserva
    public Reserva(Long reservaId, String email, LocalDateTime fechaCancel){
        this.reservaId = reservaId;
        this.email = email;
        this.fechaCancelacion=fechaCancel;
    }

    //Constructores usados para obter información sobre reservas ou engadir reservas
    public Reserva(Long reservaId, String email, Long excId, short numPlazas, String tarjetaCredito, LocalDateTime fecha,float precioCompra) {
        this.reservaId = reservaId;
        this.email = email;
        this.excId = excId;
        this.numPlazas = numPlazas;
        this.tarjetaCredito = tarjetaCredito;
        this.fecha = fecha;
        this.fechaCancelacion = null;
        this.precioCompra = precioCompra;
    }


    public Reserva(Long reservaId, String email, Long excId, short numPlazas, String tarjetaCredito,
                   LocalDateTime fecha, LocalDateTime canecellationDate, float precioCompra) {
        this.reservaId = reservaId;
        this.email = email;
        this.excId = excId;
        this.numPlazas = numPlazas;
        this.tarjetaCredito = tarjetaCredito;
        this.fecha = fecha;
        this.fechaCancelacion = canecellationDate;
        this.precioCompra = precioCompra;
    }
    public Long getReservaId() {
        return reservaId;
    }

    public String getEmail() {
        return email;
    }

    public Long getExcId() {
        return excId;
    }

    public short getNumPlazas() {
        return numPlazas;
    }

    public String getTarjetaCredito() {
        return tarjetaCredito;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public LocalDateTime getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(LocalDateTime fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public float getPrecioCompra() { return precioCompra;}

    public void setPrecioCompra(float precioCompra) { this.precioCompra = precioCompra;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return numPlazas == reserva.numPlazas && Double.compare(reserva.precioCompra, precioCompra) == 0 && Objects.equals(reservaId, reserva.reservaId) && Objects.equals(email, reserva.email) && Objects.equals(excId, reserva.excId) && Objects.equals(tarjetaCredito, reserva.tarjetaCredito) && Objects.equals(fecha, reserva.fecha) && Objects.equals(fechaCancelacion, reserva.fechaCancelacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservaId, email, excId, numPlazas, tarjetaCredito, fecha, fechaCancelacion, precioCompra);
    }
}

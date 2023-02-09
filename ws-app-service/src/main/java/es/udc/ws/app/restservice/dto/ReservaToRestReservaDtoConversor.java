package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.reserva.Reserva;

import java.util.ArrayList;
import java.util.List;

public class ReservaToRestReservaDtoConversor {
    public static RestReservaDto toRestReservaDto(Reserva reserva){
        return new RestReservaDto(reserva.getReservaId(),reserva.getEmail(), reserva.getExcId(),
                reserva.getNumPlazas(), reserva.getTarjetaCredito().substring(reserva.getTarjetaCredito().length()-4),
                reserva.getFecha(),reserva.getFechaCancelacion() , reserva.getPrecioCompra());
    }

    public static List<RestReservaDto> toRestReservaDto (List<Reserva>reservas){
        List<RestReservaDto> list = new ArrayList<>(reservas.size());
        for(Reserva reserva : reservas){
            list.add(toRestReservaDto(reserva));
        }
        return list;
    }
}

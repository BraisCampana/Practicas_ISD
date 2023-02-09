package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.thrift.ThriftExcursionDto;

import java.time.LocalDateTime;

public class ExcursionToThriftExcursionDtoConversor {

    public static ThriftExcursionDto toThriftExcursionDto(Excursion exc){
        return new ThriftExcursionDto(exc.getExcId(), exc.getCiudad(), exc.getDescripcion(),exc.getFechaCelebracion().toString(),
                exc.getPrecio(), exc.getNumMaxPers(), exc.getNumPers());
    }

    public static Excursion toExcursion(ThriftExcursionDto exc) {
        return new Excursion(exc.getExcId(),exc.getCiudad(), exc.getDescripcion(), LocalDateTime.parse(exc.getFechaCelebracion()),
                ((float) exc.getPrecio()), (short) exc.getNumMaxPers(), (short) exc.getNumPers());
    }

}
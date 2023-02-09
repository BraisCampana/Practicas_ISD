package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.thrift.ThriftExcursionDto;
import es.udc.ws.app.thrift.ThriftExcursionService;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ClientExcursionDtoToThriftExcursionDtoConversor {
    public static ThriftExcursionDto toThriftExcursionDto(ClientExcursionDto exc){
        Long excId = exc.getExcId();

        return new ThriftExcursionDto( excId == null ? -1 : excId.longValue(), exc.getCiudad(),
                exc.getDescripcion(), String.valueOf(exc.getFechaCelebracion()),
                exc.getPrecio(), exc.getNumMaxPers(), exc.getNumPers());
    }

    private static ClientExcursionDto toClientExcursionDto(ThriftExcursionDto excThr){
        return new ClientExcursionDto(
                excThr.getExcId(), excThr.getCiudad(), excThr.getDescripcion(),
                LocalDateTime.parse(excThr.getFechaCelebracion()),
                Float.valueOf((float) excThr.getPrecio()),
                (short) excThr.getNumMaxPers(), (short) excThr.getNumPers()
        );
    }
}

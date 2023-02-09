package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.excursion.Excursion;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ExcursionToRestExcursionDtoConversor {

    public static List<RestExcursionDto> toRestExcursionDtos(List<Excursion> excursiones) {
        List<RestExcursionDto> excursionDtos = new ArrayList<>(excursiones.size());
        for (int i = 0; i < excursiones.size(); i++) {
            Excursion excursion = excursiones.get(i);
            excursionDtos.add(toRestExcursionDto(excursion));
        }
        return excursionDtos;
    }

    public static RestExcursionDto toRestExcursionDto(Excursion excursion) {
        return new RestExcursionDto(excursion.getExcId(), excursion.getCiudad(), excursion.getDescripcion(),
                excursion.getFechaCelebracion().toString(), excursion.getPrecio(), excursion.getNumMaxPers(),excursion.getNumPers());
    }

    public static Excursion toExcursion(RestExcursionDto excursion) {
        return new Excursion(excursion.getExcId(), excursion.getCiudad(), excursion.getDescripcion(), LocalDateTime.parse(excursion.getFechaCelebracion()),
                excursion.getPrecio(), excursion.getNumMaxPers(), excursion.getNumPers());
    }

}

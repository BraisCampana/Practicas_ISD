package es.udc.ws.app.restservice.json;

import es.udc.ws.app.restservice.dto.RestReservaDto;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public class JsonToRestReservaDtoConversor {

    public static ObjectNode toObjectNode (RestReservaDto reserva){
        ObjectNode reservaNode = JsonNodeFactory.instance.objectNode();

        if (reserva.getReservaId() != null) reservaNode.put("reservaId",reserva.getReservaId());

        reservaNode.put("userMail",reserva.getEmail())
                .put("excursionId",reserva.getExcId())
                .put("plazas",reserva.getNumPlazas())
                .put("lastDigitsCreditCard",reserva.getLast4digTarjeta())
                .put("fechaCreacion",reserva.getFechaCreacion().toString())
                .put("fechaCancelación",(reserva.getFechaCancelacion()!=null)?reserva.getFechaCancelacion().toString():null)
                .put("precioCompra",reserva.getPrecioCompra());
        return reservaNode;
    }

    public static ObjectNode reservaIdToObjectNode(long id){
        ObjectNode node = JsonNodeFactory.instance.objectNode();
        node.put("reservaId",id);
        return node;
    }

    public static ArrayNode toArrayNode(List<RestReservaDto> reservaDtoList){
        ArrayNode reservaNode = JsonNodeFactory.instance.arrayNode();

        for (RestReservaDto reserva : reservaDtoList){
            reservaNode.add(toObjectNode(reserva));
        }
        return reservaNode;
    }
}

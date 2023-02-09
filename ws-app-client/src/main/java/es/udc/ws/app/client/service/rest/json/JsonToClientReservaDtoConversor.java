package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientReservaDtoConversor {

    public static ClientReservaDto toClientReservaDto(InputStream jsonReserva)throws ParsingException{
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);

            return toClientReservaDto(rootNode);
        } catch (ParsingException e) {
            throw e;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    public static Long toLongAddReserva(InputStream jsonReserva){
        try{
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);
            if(rootNode.getNodeType() != JsonNodeType.OBJECT){
                throw new ParsingException("Unrecognized JSON (object expected)");
            }else{
                return rootNode.get("reservaId").longValue();
            }
        } catch (ParsingException e) {
            throw e;
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static ClientReservaDto toClientReservaDto(JsonNode jsonNode) throws ParsingException{
        if (jsonNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode reservaObj = (ObjectNode) jsonNode;
            JsonNode reservaIdNode = reservaObj.get("reservaId");
            Long reservaId = (reservaIdNode != null) ? reservaIdNode.longValue() : null;
            String email = reservaObj.get("userMail").textValue().trim();
            Long excursionId = reservaObj.get("excursionId").longValue();
            short places = reservaObj.get("plazas").shortValue();
            String lastDigitsCreditCard = reservaObj.get("lastDigitsCreditCard").textValue().trim();
            String stringfechaCrea = reservaObj.get("fechaCreacion").textValue().trim();
            String stringfechaCanc =  (reservaObj.get("fechaCancelación").getNodeType() != JsonNodeType.NULL) ? reservaObj.get("fechaCancelación").textValue().trim() : null;
            double prize = reservaObj.get("precioCompra").doubleValue();
            return new ClientReservaDto(reservaId,email,excursionId,places,lastDigitsCreditCard,
                    LocalDateTime.parse(stringfechaCrea),(stringfechaCanc!=null)?LocalDateTime.parse(stringfechaCanc):null,prize);
        }
    }

    public static List<ClientReservaDto> toClientReservaDtos(InputStream jsonReserva) throws ParsingException{
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReserva);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode moviesArray = (ArrayNode) rootNode;
                List<ClientReservaDto> reservaDtos = new ArrayList<>(moviesArray.size());
                for (JsonNode reservaNode : moviesArray) {
                    reservaDtos.add(toClientReservaDto(reservaNode));
                }
                return reservaDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
}

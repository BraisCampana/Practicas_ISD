package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientExcursionDtoConversor {

    public static ObjectNode toObjectNode(ClientExcursionDto excursion) throws IOException {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if (excursion.getExcId() != null) {
            excursionObject.put("excursionId", excursion.getExcId());
        }
        excursionObject.put("ciudad", excursion.getCiudad()).
                put("descripcion", excursion.getDescripcion()).
                put("fechaCelebracion", excursion.getFechaCelebracion().toString()).
                put("precio", excursion.getPrecio()).
                put("numMaxPers", excursion.getNumMaxPers()).
                put("numPers",excursion.getNumPers());

        return excursionObject;
    }

    public static List<ClientExcursionDto> toClientExcursionDtos(InputStream jsonExcursiones) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursiones);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode excursionesArray = (ArrayNode) rootNode;
                List<ClientExcursionDto> excursionDtos = new ArrayList<>(excursionesArray.size());
                for (JsonNode excursionNode : excursionesArray) {
                    excursionDtos.add(toClientExcursionDto_(excursionNode));
                }

                return excursionDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }


    public static ClientExcursionDto toClientExcursionDto(InputStream jsonExcursion) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientExcursionDto_(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientExcursionDto toClientExcursionDto_(JsonNode excursionNode) throws ParsingException {
        if (excursionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode excursionObject = (ObjectNode) excursionNode;

            JsonNode excursionIdNode = excursionObject.get("excursionId");
            Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

            String ciudad = excursionObject.get("ciudad").textValue().trim();
            String descripcion = excursionObject.get("descripcion").textValue().trim();
            String fechaCelebracion = excursionObject.get("fechaCelebracion").textValue().trim();
            float precio = excursionObject.get("precio").floatValue();
            Short numMaxPers = excursionObject.get("numMaxPers").shortValue();
            Short numPers = excursionObject.get("numPers").shortValue();

            return new ClientExcursionDto(excursionId, ciudad, descripcion, LocalDateTime.parse(fechaCelebracion), precio,
                    numMaxPers,numPers);
        }
    }


}

package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;
import es.udc.ws.app.restservice.dto.RestExcursionDto;

import java.io.InputStream;
import java.util.List;

public class JsonToRestExcursionDtoConversor {

    public static ObjectNode toObjectNode(RestExcursionDto excursion) {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if (excursion.getExcId() != null) {
            excursionObject.put("excursionId", excursion.getExcId());
        }
        excursionObject.put("ciudad", excursion.getCiudad()).
                put("descripcion", excursion.getDescripcion()).
                put("fechaCelebracion", excursion.getFechaCelebracion()).
                put("precio", excursion.getPrecio()).
                put("numMaxPers",excursion.getNumMaxPers()).
                put("numPers", excursion.getNumPers());

        return excursionObject;
    }

    public static ArrayNode toArrayNode(List<RestExcursionDto> excursiones) {

        ArrayNode excursionesNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < excursiones.size(); i++) {
            RestExcursionDto movieDto = excursiones.get(i);
            ObjectNode excursionObject = toObjectNode(movieDto);
            excursionesNode.add(excursionObject);
        }

        return excursionesNode;
    }

    public static RestExcursionDto toRestExcursionDto(InputStream jsonExcursion) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode excursionObject = (ObjectNode) rootNode;

                JsonNode excursionIdNode = excursionObject.get("excursionId");
                Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

                String ciudad = excursionObject.get("ciudad").textValue().trim();
                String descripcion = excursionObject.get("descripcion").textValue().trim();
                String fechaCelebracion =  excursionObject.get("fechaCelebracion").textValue().trim();
                float precio = excursionObject.get("precio").floatValue();
                Short numMaxPers = excursionObject.get("numMaxPers").shortValue();



                return new RestExcursionDto(excursionId, ciudad, descripcion, fechaCelebracion, precio, numMaxPers);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}

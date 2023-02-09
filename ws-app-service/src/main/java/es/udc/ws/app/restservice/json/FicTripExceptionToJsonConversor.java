package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.excursionservice.exceptions.NotEnoughNoticeToUpdateException;
import es.udc.ws.app.model.excursionservice.exceptions.TooLateToCancelReservaException;

public class FicTripExceptionToJsonConversor {
    public static ObjectNode toTooLateToCancelReserveException(TooLateToCancelReservaException ex){
        ObjectNode exception = JsonNodeFactory.instance.objectNode();
        exception.put("errorType","TooLateToCancelReserve");
        exception.put("reservaId",ex.getReservaId());

        return exception;
    }

    public static ObjectNode toNotEnoughNoticeToUpdateException(NotEnoughNoticeToUpdateException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "NotEnoughNoticeToUpdate");
        exceptionObject.put("excursionId", (ex.getExcId() != null) ? ex.getExcId() : null);

        return exceptionObject;
    }
}

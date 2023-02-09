package es.udc.ws.app.restservice.servlets;


import es.udc.ws.app.restservice.dto.ReservaToRestReservaDtoConversor;
import es.udc.ws.app.restservice.dto.RestReservaDto;
import es.udc.ws.app.restservice.json.FicTripExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestReservaDtoConversor;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.TooLateToCancelReservaException;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("serial")
public class ReservaServlet extends RestHttpServletTemplate {
    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);
        String userMail = ServletUtils.getMandatoryParameter(req,"userMail");
        Long excursionId = ServletUtils.getMandatoryParameterAsLong(req,"excursionId");
        Long numPlazas = ServletUtils.getMandatoryParameterAsLong(req,"numPlazas");
        String creditCardNumber = ServletUtils.getMandatoryParameter(req,"creditCardNumber");

        Long reservaId = ExcursionServiceFactory.getService().addReserva(userMail,excursionId,numPlazas.shortValue(),creditCardNumber);

        String url = ServletUtils.normalizePath(req.getRequestURL().toString()+"/"+reservaId.toString());
        Map<String,String> headers = new HashMap<>(1);
        headers.put("Location",url);
        ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_CREATED,
                JsonToRestReservaDtoConversor.reservaIdToObjectNode(reservaId),headers);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, InputValidationException {
        ServletUtils.checkEmptyPath(req);
        String userMail = ServletUtils.getMandatoryParameter(req, "userMail");
        List<Reserva> reservaList = ExcursionServiceFactory.getService().findReservaByUserMail(userMail);

        List<RestReservaDto> reservaDtos= ReservaToRestReservaDtoConversor.toRestReservaDto(reservaList);

        ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_OK,JsonToRestReservaDtoConversor.toArrayNode(reservaDtos),null);
    }

    @Override
    protected void processDelete(HttpServletRequest req, HttpServletResponse resp) throws InputValidationException, InstanceNotFoundException, IOException {
        String userMail = ServletUtils.getMandatoryParameter(req,"userMail");
        long reservaId = ServletUtils.getIdFromPath(req,"reserva");

        try{
            ExcursionServiceFactory.getService().cancelReserva(reservaId, userMail);
        }catch (TooLateToCancelReservaException e){
            ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_FORBIDDEN, FicTripExceptionToJsonConversor.toTooLateToCancelReserveException(e),null);
        }
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }
}

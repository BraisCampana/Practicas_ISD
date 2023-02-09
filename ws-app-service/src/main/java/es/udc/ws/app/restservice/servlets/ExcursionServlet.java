package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.NotEnoughNoticeToUpdateException;
import es.udc.ws.app.restservice.dto.ExcursionToRestExcursionDtoConversor;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.app.restservice.json.FicTripExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestExcursionDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcursionServlet extends RestHttpServletTemplate {

    @Override
    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        ServletUtils.checkEmptyPath(req);

        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        excursion = ExcursionServiceFactory.getService().addExcursion(excursion);

        excursionDto = ExcursionToRestExcursionDtoConversor.toRestExcursionDto(excursion);
        String excursionURL = ServletUtils.normalizePath(req.getRequestURL().toString()) + "/" + excursion.getExcId();
        Map<String, String> headers = new HashMap<>(1);
        headers.put("Location", excursionURL);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED,
                JsonToRestExcursionDtoConversor.toObjectNode(excursionDto), headers);
    }

    @Override
    protected void processPut(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        Long excursionId = ServletUtils.getIdFromPath(req, "excursion");

        RestExcursionDto excursionDto = JsonToRestExcursionDtoConversor.toRestExcursionDto(req.getInputStream());
        if (!excursionId.equals(excursionDto.getExcId())) {
            throw new InputValidationException("Invalid Request: invalid excursionId");
        }
        Excursion excursion = ExcursionToRestExcursionDtoConversor.toExcursion(excursionDto);

        try {
            ExcursionServiceFactory.getService().updateExcursion(excursion);
        } catch (NotEnoughNoticeToUpdateException e) {
            ServletUtils.writeServiceResponse(resp,HttpServletResponse.SC_FORBIDDEN, FicTripExceptionToJsonConversor.toNotEnoughNoticeToUpdateException(e),null);
        } catch (InputValidationException e) {
            throw e;
        }

        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);
    }

    @Override
    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException, InstanceNotFoundException {
        ServletUtils.checkEmptyPath(req);
        String dateIni = req.getParameter("dateini");
        String dateFin = req.getParameter("datefin");
        String ciudad = req.getParameter("ciudad");

        LocalDateTime dateIniParsed = LocalDate.parse(dateIni).atStartOfDay();
        LocalDateTime dateFinParsed = LocalDate.parse(dateFin).atStartOfDay();



        List<Excursion> excursiones = ExcursionServiceFactory.getService().findExcursion(ciudad, dateIniParsed, dateFinParsed);

        List<RestExcursionDto> excursionDtos = ExcursionToRestExcursionDtoConversor.toRestExcursionDtos(excursiones);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestExcursionDtoConversor.toArrayNode(excursionDtos), null);
    }



}

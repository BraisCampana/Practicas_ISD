package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientTooLateToCancelReservaException;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExcursionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientReservaDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

public class RestClientExcursionService implements ClientExcursionService {
    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientExcursionService.endpointAddress";
    private String endpointAddress;

    @Override
    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "excursion").
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientExcursionDtoConversor.toClientExcursionDto(response.getEntity().getContent()).getExcId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException,
            InstanceNotFoundException {

        try {

            HttpResponse response = Request.Put(getEndpointAddress() + "excursion/" + excursion.getExcId()).
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        } catch (InputValidationException | InstanceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientExcursionDto> findExcursiones(LocalDate dateini, LocalDate datefin, String ciudad) throws InputValidationException {
        try {
            if (dateini == null) {
                throw new InputValidationException("Date can not be null");
            }
            if (datefin == null) {
                throw new InputValidationException("Date can not be null");
            }
            if ((ciudad == null) || (ciudad.strip() == "")) {
                throw new InputValidationException("Ciudad can not be null nor empty");
            }

            HttpResponse response = null;
            String dateIniStr = dateini.toString();
            String dateFinStr = datefin.toString();

            String requestStr = getEndpointAddress() + "/excursion?dateini="
                    + URLEncoder.encode(dateIniStr, StandardCharsets.UTF_8)
                    + "&datefin=" + URLEncoder.encode(dateFinStr, StandardCharsets.UTF_8)
                    + "&ciudad=" + URLEncoder.encode(ciudad, StandardCharsets.UTF_8);

            response = Request.Get(requestStr).execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientExcursionDtoConversor.toClientExcursionDtos(
                    response.getEntity().getContent());
        } catch (InputValidationException e){
            throw e;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    @Override
    public Long addReserva(String userMail, Long excursionId, String creditCardNumber, Short places) throws InstanceNotFoundException, InputValidationException {
        try {
            HttpResponse response = Request.Post(getEndpointAddress() + "reserva")
                    .bodyForm(Form.form()
                            .add("userMail", userMail)
                            .add("excursionId", excursionId.toString())
                            .add("numPlazas", places.toString())
                            .add("creditCardNumber", creditCardNumber)
                            .build()).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);
            return JsonToClientReservaDtoConversor.toLongAddReserva(response.getEntity().getContent());
        } catch(InstanceNotFoundException | InputValidationException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelReserva(Long reserveId, String userMail)throws InputValidationException,InstanceNotFoundException, ClientTooLateToCancelReservaException {
        try{
            HttpResponse response = Request.Delete(getEndpointAddress()+"reserva/"+reserveId+"?userMail="+userMail).execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT,response);
        }catch (InputValidationException | InstanceNotFoundException | ClientTooLateToCancelReservaException e){
            throw e;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ClientReservaDto> findReserva(String userMail) {
        try{
            HttpResponse response = Request.Get(getEndpointAddress()+"reserva?userMail="+userMail)
                    .execute().returnResponse();
            validateStatusCode(HttpStatus.SC_OK,response);
            return JsonToClientReservaDtoConversor.toClientReservaDtos(response.getEntity().getContent());
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientExcursionDto excursion) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientExcursionDtoConversor.toObjectNode(excursion));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, org.apache.http.HttpResponse response) throws Exception {

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            /* Success? */
            if (statusCode == successCode) {
                return;
            }
            /* Handler error. */
            switch (statusCode) {
                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());
                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

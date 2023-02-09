package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientTooLateToCancelReservaException;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDate;
import java.util.List;

public interface ClientExcursionService {
    public Long addExcursion(ClientExcursionDto excursion)
            throws InputValidationException;

    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException,
            InstanceNotFoundException;

    public List<ClientExcursionDto> findExcursiones(LocalDate dateini, LocalDate datefin, String ciudad)
            throws InputValidationException;

    public Long addReserva(String userMail, Long excursionId, String creditCardNumber, Short places) throws InstanceNotFoundException, InputValidationException;

    public void cancelReserva(Long reserveId, String userMail) throws InstanceNotFoundException, ClientTooLateToCancelReservaException, InputValidationException;

    public List<ClientReservaDto> findReserva(String userMail);

}

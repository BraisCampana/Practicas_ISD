package es.udc.ws.app.model.excursionservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

public interface ExcursionService {

    public Excursion addExcursion(Excursion excursion) throws InputValidationException;

    public void updateExcursion(Excursion excursion) throws InputValidationException, InstanceNotFoundException, NotEnoughNoticeToUpdateException;

    public List<Excursion> findExcursion(String ciudad, LocalDateTime fechaInicial, LocalDateTime fechaFinal) throws InstanceNotFoundException, InputValidationException;

    public long addReserva(String userMail, long idExcursion, short numPlazas, String tarjetaBancaria) throws InputValidationException, InstanceNotFoundException;

    public void cancelReserva(long idReserva, String email) throws InputValidationException, InstanceNotFoundException, TooLateToCancelReservaException;

    public List<Reserva> findReservaByUserMail(String email);
}

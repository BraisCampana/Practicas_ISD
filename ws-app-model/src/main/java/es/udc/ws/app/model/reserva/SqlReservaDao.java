package es.udc.ws.app.model.reserva;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.Connection;
import java.util.List;

public interface SqlReservaDao {
    public Reserva create(Connection connection, Reserva reserve);

    public List<Reserva> find(Connection connection, String userMail);

    public Reserva findById(Connection connection, Long reserveId) throws InstanceNotFoundException;

    public void cancel(Connection connection, Reserva reserve) throws InstanceNotFoundException;

    public void remove(Connection connection, Long reserveId) throws InstanceNotFoundException;
}

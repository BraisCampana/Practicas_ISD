package es.udc.ws.app.model.reserva;

import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlReservaDao implements SqlReservaDao{
    protected AbstractSqlReservaDao(){}

    @Override
    public List<Reserva> find(Connection connection, String userMail) {
        String queryString = "SELECT * FROM Reserva WHERE email = ? ORDER BY reserveDate DESC";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            /* Fill "preparedStatement". */
            preparedStatement.setString(1,userMail);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            /* Get results. */
            List<Reserva> listReservas = new ArrayList<>();
            while(resultSet.next()){
                int i = 1;
                long reserveID = resultSet.getLong(i++);
                String email = resultSet.getString(i++);
                long excursionID = resultSet.getLong(i++);
                short numSeats = resultSet.getShort(i++);
                String creditCard = resultSet.getString(i++);
                Timestamp dateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime reserveDate = dateAsTimestamp.toLocalDateTime();
                dateAsTimestamp = resultSet.getTimestamp(i++);
                LocalDateTime cancellationDate = null;
                if(dateAsTimestamp != null){
                    cancellationDate = dateAsTimestamp.toLocalDateTime();
                }
                float precioCompra = resultSet.getFloat(i);
                listReservas.add(new Reserva(reserveID,email,excursionID,numSeats,creditCard,reserveDate,cancellationDate,precioCompra));
            }
            return listReservas;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancel(Connection connection, Reserva Reserva) throws InstanceNotFoundException {
        String queryString = "UPDATE Reserva SET cancellationDate = ?"+
                "WHERE reserveID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(Reserva.getFechaCancelacion()));
            preparedStatement.setLong(i,Reserva.getReservaId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(Reserva.getReservaId(), Reserva.class.getName());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(Connection connection, Long reserveId) throws InstanceNotFoundException {
        /* Create "queryString". */
        String queryString = "DELETE FROM Reserva WHERE reserveID = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            preparedStatement.setLong(1, reserveId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(reserveId,
                        Reserva.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Reserva findById(Connection connection, Long reserveId) throws InstanceNotFoundException{
        /* Create "queryString". */
        String queryString = "SELECT * FROM Reserva WHERE reserveId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)){
            /* Fill "preparedStatement". */
            preparedStatement.setLong(1,reserveId);

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) throw new InstanceNotFoundException(reserveId,Reserva.class.getName());

            /* Get results. */

            int i = 1;
            long reserveID = Long.valueOf(resultSet.getLong(i++));
            String email = resultSet.getString(i++);
            long excursionID = resultSet.getLong(i++);
            short numSeats = resultSet.getShort(i++);
            String creditCard = resultSet.getString(i++);
            Timestamp dateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime reserveDate = dateAsTimestamp.toLocalDateTime();
            dateAsTimestamp = resultSet.getTimestamp(i++);
            LocalDateTime cancellationDate = null;
            if(dateAsTimestamp != null){
                cancellationDate = dateAsTimestamp.toLocalDateTime();
            }
            float precioCompra = resultSet.getFloat(i);

            return new Reserva(reserveID,email,excursionID,numSeats,creditCard,reserveDate,cancellationDate,precioCompra);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

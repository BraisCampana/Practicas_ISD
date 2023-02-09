package es.udc.ws.app.model.reserva;

import java.sql.*;

public class Jdbc3CcSqlReservaDao extends AbstractSqlReservaDao{
    @Override
    public Reserva create(Connection connection, Reserva reserva) {
        /* Create "queryString". */
        String queryString = "INSERT INTO Reserva"
                + " (email, excursionID, numSeats, creditCard, reserveDate, precioCompra)"
                + " VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, reserva.getEmail());
            preparedStatement.setLong(i++, reserva.getExcId());
            preparedStatement.setShort(i++, reserva.getNumPlazas());
            preparedStatement.setString(i++, reserva.getTarjetaCredito());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(reserva.getFecha()));
            preparedStatement.setFloat(i,reserva.getPrecioCompra());

            /* Execute query. */
            preparedStatement.executeUpdate();

            /* Get generated identifier. */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long reserveID = resultSet.getLong(1);

            /* Return reserva. */
            return new Reserva(reserveID, reserva.getEmail(), reserva.getExcId(),
                    reserva.getNumPlazas(), reserva.getTarjetaCredito(),
                    reserva.getFecha(),null,reserva.getPrecioCompra());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package es.udc.ws.app.model.excursion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

public class Jdbc3CcSqlExcursionDao extends AbstractSqlExcursionDao{

    @Override
    public Excursion create(Connection connection, Excursion excursion){
        String queryString = "INSERT INTO Excursion "
                + " (ciudad, descripcion, fechaCelebracion, precio, numMaxPersonas, numPersonas, fechaCreacion)"
                + " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                queryString, Statement.RETURN_GENERATED_KEYS)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, excursion.getCiudad());
            preparedStatement.setString(i++, excursion.getDescripcion());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaCelebracion()));
            preparedStatement.setFloat(i++, excursion.getPrecio());
            preparedStatement.setShort(i++, excursion.getNumMaxPers());
            preparedStatement.setShort(i++, excursion.getNumPers());
            preparedStatement.setTimestamp(i++, Timestamp.valueOf(excursion.getFechaCreacion()));

            /* Lo ejecuto */
            preparedStatement.executeUpdate();

            /* genero el id */
            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                throw new SQLException(
                        "JDBC driver did not return generated key.");
            }
            Long excursionId = resultSet.getLong(1);

            /* Devolver excursion. */
            return new Excursion(excursionId, excursion.getCiudad(), excursion.getDescripcion(),
                    excursion.getFechaCelebracion(), excursion.getPrecio(), excursion.getNumMaxPers(),
                    excursion.getNumPers(), excursion.getFechaCreacion());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

}

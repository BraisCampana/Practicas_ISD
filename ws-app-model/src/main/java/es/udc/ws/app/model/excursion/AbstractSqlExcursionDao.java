package es.udc.ws.app.model.excursion;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSqlExcursionDao implements SqlExcursionDao {

    protected AbstractSqlExcursionDao() {

    }

    @Override
    public void update (Connection connection, Excursion excursion) throws InstanceNotFoundException, InputValidationException {

        /* Create "queryString". */
        String queryString = "UPDATE Excursion"
                + " SET ciudad = ?, descripcion = ?, fechaCelebracion = ?, precio = ?, "
                + "  numMaxPersonas = ?, numPersonas=? WHERE excursionId = ?";

        if(findById(connection,excursion.getExcId()).getNumPers()>excursion.getNumMaxPers())
            throw new InputValidationException("Número máximo de plaza inválido");

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setString(i++, excursion.getCiudad());
            preparedStatement.setString(i++, excursion.getDescripcion());
            preparedStatement.setDate(i++, Date.valueOf(excursion.getFechaCelebracion().toLocalDate()));
            preparedStatement.setFloat(i++, excursion.getPrecio());
            preparedStatement.setShort(i++, excursion.getNumMaxPers());
            preparedStatement.setShort(i++,excursion.getNumPers());
            preparedStatement.setLong(i, excursion.getExcId());

            /* Execute query. */
            int updatedRows = preparedStatement.executeUpdate();

            if (updatedRows == 0) {
                throw new InstanceNotFoundException(excursion.getExcId(),
                        Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Excursion> find(Connection connection, String ciudad, LocalDateTime fechaInicial, LocalDateTime fechaFinal){
        String queryString = "SELECT excursionId, ciudad, descripcion, "
                + " fechaCelebracion, precio, numMaxPersonas, numPersonas, fechaCreacion FROM Excursion";
        if (ciudad != null){
            queryString += " WHERE ciudad = ?";
        }

        if (fechaInicial != null){
            queryString += " AND  fechaCelebracion > ?";
        }

        if(fechaFinal != null){
            queryString += " AND fechaCelebracion < ?";
        }

        try(PreparedStatement preparedStatement = connection.prepareStatement(queryString)){

            int i = 1;
            preparedStatement.setString(i++, ciudad);
            if(fechaInicial != null) {
                preparedStatement.setDate(i++, Date.valueOf(fechaInicial.toLocalDate()));
            }
            if(fechaFinal != null) {
                preparedStatement.setDate(i++, Date.valueOf(fechaFinal.toLocalDate()));
            }

            /* Ejecuto query */
            ResultSet resultSet = preparedStatement.executeQuery();

            List<Excursion> excursiones = new ArrayList<Excursion>();

            // cojo los datos del resultSet y los pongo en el ArrayList
            while (resultSet.next()) {
                i = 1;
                Long excursionId = Long.valueOf(resultSet.getLong(i++));
                String ciudadRS = resultSet.getString(i++);
                String descripcion = resultSet.getString(i++);
                LocalDateTime fechaCelebracion = resultSet.getTimestamp(i++).toLocalDateTime();
                float precio = resultSet.getFloat(i++);
                short numMaxPersonas = resultSet.getShort(i++);
                short numPersonas = resultSet.getShort(i++);
                LocalDateTime fechaCreacion = resultSet.getTimestamp(i++).toLocalDateTime();
                if(fechaCelebracion.minusHours(24).isAfter(LocalDateTime.now())) {
                    excursiones.add(new Excursion(excursionId, ciudadRS, descripcion, fechaCelebracion,
                            precio, numMaxPersonas, numPersonas, fechaCreacion));
                }
            }

            // devuelvo el ArrayList
            return excursiones;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Excursion findById(Connection connection, Long excursionId)
            throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "SELECT ciudad, descripcion, "
                + " fechaCelebracion, precio, numMaxPersonas, numPersonas, fechaCreacion FROM Excursion WHERE excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId.longValue());

            /* Execute query. */
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new InstanceNotFoundException(excursionId,
                        Excursion.class.getName());
            }

            /* Get results. */
            i = 1;

            String ciudadRS = resultSet.getString(i++);
            String descripcion = resultSet.getString(i++);
            LocalDateTime fechaCelebracion = resultSet.getTimestamp(i++).toLocalDateTime();
            float precio = resultSet.getFloat(i++);
            short numMaxPersonas = resultSet.getShort(i++);
            short numPersonas = resultSet.getShort(i++);
            LocalDateTime fechaCreacion = resultSet.getTimestamp(i++).toLocalDateTime();

            return new Excursion(excursionId, ciudadRS, descripcion, fechaCelebracion,
                    precio, numMaxPersonas, numPersonas, fechaCreacion);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public void remove (Connection connection, Long excursionId) throws InstanceNotFoundException {

        /* Create "queryString". */
        String queryString = "DELETE FROM Excursion WHERE" + " excursionId = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(queryString)) {

            /* Fill "preparedStatement". */
            int i = 1;
            preparedStatement.setLong(i++, excursionId);

            /* Execute query. */
            int removedRows = preparedStatement.executeUpdate();

            connection.commit();

            if (removedRows == 0) {
                throw new InstanceNotFoundException(excursionId, Excursion.class.getName());
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

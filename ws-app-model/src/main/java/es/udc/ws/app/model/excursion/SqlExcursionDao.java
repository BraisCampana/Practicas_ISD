package es.udc.ws.app.model.excursion;

import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

public interface SqlExcursionDao {

    public Excursion create(Connection connection, Excursion excursion);


    public void update(Connection connection, Excursion excursion)
            throws InstanceNotFoundException, InputValidationException;

    //devuelve las excursiones según la ciudad (que aun se puedan reservar),
    // y opcionalmente una orquilla de fechas
    public List<Excursion> find(Connection connection,
                                String ciudad, LocalDateTime fechaInicial, LocalDateTime fechaFinal);

    public Excursion findById(Connection connection, Long excursionId)
            throws InstanceNotFoundException;

    public void remove (Connection connection, Long excursionId) throws InstanceNotFoundException;




}

package es.udc.ws.app.model.excursionservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.validation.PropertyValidator;



import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static es.udc.ws.app.model.util.ModelConstants.*;


public class ExcursionServiceImpl implements ExcursionService{

    private final DataSource dataSource;
    private SqlExcursionDao excursionDao = null;
    private SqlReservaDao reservaDao = null;

    public ExcursionServiceImpl() {
        dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        excursionDao = SqlExcursionDaoFactory.getDao();
        reservaDao = SqlReservaDaoFactory.getDao();
    }

    private void validateExcursion(Excursion excursion) throws InputValidationException {
        PropertyValidator.validateMandatoryString("ciudad", excursion.getCiudad());
        PropertyValidator.validateMandatoryString("descripcion", excursion.getDescripcion());
        PropertyValidator.validateDouble("precio", excursion.getPrecio(), 0, MAX_PRICE);
        PropertyValidator.validateLong("numPersonas", excursion.getNumPers(), 0, MAX_PERS);
        PropertyValidator.validateLong("numMaxPersonas", excursion.getNumMaxPers(), 0, MAX_PERS);
    }

    private void validateReserva(Reserva reserva) throws InputValidationException{
        PropertyValidator.validateMandatoryString("email",reserva.getEmail());
        PropertyValidator.validateLong("numPlazas",(long)reserva.getNumPlazas(),MIN_NUMSEATS_RESERVA,MAX_NUMSEATS_RESERVA);
        PropertyValidator.validateMandatoryString("tarjetaCredito",reserva.getTarjetaCredito());
    }

    @Override
    public Excursion addExcursion(Excursion excursion) throws InputValidationException {

        validateExcursion(excursion);
        excursion.setFechaCreacion(LocalDateTime.now().withNano(0));

        if(excursion.getFechaCelebracion().minusHours(72).isBefore(excursion.getFechaCreacion())){
            throw new InputValidationException("Excursion con Id " + excursion.getExcId() + " tiene una fecha de Celebracion con poca antelación con respecto a su fecha de creación.");
        }

        try(Connection connection =dataSource.getConnection()){
            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Excursion createdExcursion = excursionDao.create(connection, excursion);

                /* Commit. */
                connection.commit();

                return createdExcursion;

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void updateExcursion(Excursion excursion)throws InputValidationException, InstanceNotFoundException,
            NotEnoughNoticeToUpdateException {
        validateExcursion(excursion);

        Excursion excursionAModificar = findExcursionById(excursion.getExcId());

        // si no es 72 horas antes de la celebración no se podrá actualizar y salta excepción
        if(excursionAModificar.getFechaCelebracion().minusHours(72).isBefore(LocalDateTime.now())){
            throw new NotEnoughNoticeToUpdateException(excursion.getExcId());
        }

        if(excursionAModificar.getFechaCelebracion().isAfter(excursion.getFechaCelebracion())){
            throw new InputValidationException("La fecha de celebración no puede ser anterior a \""+excursionAModificar.getFechaCelebracion()+"\"");
        }

        if(excursionAModificar.getNumPers() > excursion.getNumMaxPers()){
            throw new InputValidationException("El número de plazas maximo de la excursion id="+excursion.getExcId()+
                    " no puede ser menor que "+excursionAModificar.getNumMaxPers());
        }

        try (Connection connection = dataSource.getConnection()) {

            try {

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                excursionDao.update(connection, excursion);

                /* Commit. */
                connection.commit();

            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public List<Excursion> findExcursion(String ciudad, LocalDateTime fechaInicial, LocalDateTime fechaFinal) throws InstanceNotFoundException, InputValidationException {
        if (fechaInicial != null && fechaFinal != null) {
            if (fechaFinal.isBefore(fechaInicial)) {
                throw new InputValidationException("No se puede realizar la búsqueda por ciudad y fecha ya que la fecha final es anterior a la inicial");
            }
        }
        try (Connection connection = dataSource.getConnection()) {
            return excursionDao.find(connection, ciudad, fechaInicial, fechaFinal);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private Excursion findExcursionById(Long excursionId) throws InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            return excursionDao.findById(connection, excursionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public long addReserva(String userMail, long idExcursion, short numPlazas, String tarjetaBancaria) throws InputValidationException,InstanceNotFoundException {

        try (Connection connection = dataSource.getConnection()) {
            try {
                //Check if reserve window is open
                Excursion excursion = excursionDao.findById(connection,idExcursion);
                Reserva reserva = new Reserva(userMail,idExcursion,numPlazas,tarjetaBancaria,excursion.getPrecio());
                validateReserva(reserva);
                reserva.setFecha(LocalDateTime.now().withNano(0));

                if(excursion.getFechaCelebracion().minusHours(MAX_RESERVE_TIME_BF_EXC).isBefore(reserva.getFecha()))
                    throw new InputValidationException("Fecha reserva demasiado tarde, faltan menos de "
                            +MAX_RESERVE_TIME_BF_EXC+"h para o inicio da excursión");
                else if(numPlazas < MIN_NUMSEATS_RESERVA || numPlazas > MAX_NUMSEATS_RESERVA)
                    throw new InputValidationException("Numero de plazas da reserva invalido");
                else if(numPlazas > excursion.getNumMaxPers()-excursion.getNumPers())
                    throw new InputValidationException("No hay plazas suficientes en la excursion");
                else if(tarjetaBancaria.length()!=16)
                    throw new InputValidationException("Número de tarjeta de crédito inválida");

                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                /* Do work. */
                Reserva createdReserva = reservaDao.create(connection, reserva);

                /* Commit. */
                connection.commit();

                excursion.setNumPers((short) (excursion.getNumPers()+numPlazas));
                excursionDao.update(connection,excursion);
                connection.commit();
                return createdReserva.getReservaId();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void cancelReserva(long idReserva, String email) throws InputValidationException, InstanceNotFoundException, TooLateToCancelReservaException {
        try (Connection connection = dataSource.getConnection()) {
            try{
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                Reserva reserva = reservaDao.findById(connection,idReserva);
                Excursion excursion = excursionDao.findById(connection,reserva.getExcId());
                if(excursion.getFechaCelebracion().minusHours(MAX_CANCELATION_TIME_BF_EXC).isBefore(LocalDateTime.now()))
                    throw new TooLateToCancelReservaException(idReserva);
                else if(!Objects.equals(reserva.getEmail(), email))
                    throw new InputValidationException("La reserva no pertecene a este usuario");
                else if(reserva.getFechaCancelacion()!= null)
                    throw new InputValidationException("Reserva "+idReserva+" xa se atopa cancelada");

                //Cancel reserve
                reserva.setFechaCancelacion(LocalDateTime.now().withNano(0));
                //Borrar plazas de excursion
                excursion.setNumPers((short) (excursion.getNumPers()-reserva.getNumPlazas()));
                excursionDao.update(connection,excursion);

                //Cancel reserva
                reservaDao.cancel(connection,reserva);

                //Commit
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw e;
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Reserva> findReservaByUserMail(String email) {
        try(Connection connection = dataSource.getConnection()){
            return reservaDao.find(connection,email);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package es.udc.ws.app.test.model.appservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursion.SqlExcursionDao;
import es.udc.ws.app.model.excursion.SqlExcursionDaoFactory;
import es.udc.ws.app.model.excursionservice.ExcursionService;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.model.excursionservice.exceptions.*;
import es.udc.ws.app.model.reserva.Reserva;
import es.udc.ws.app.model.reserva.SqlReservaDao;
import es.udc.ws.app.model.reserva.SqlReservaDaoFactory;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.sql.DataSourceLocator;
import es.udc.ws.util.sql.SimpleDataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static es.udc.ws.app.model.util.ModelConstants.APP_DATA_SOURCE;
import static org.junit.jupiter.api.Assertions.*;

public class AppServiceTest {

    private final String VALID_CREDIT_CARD_NUMBER = "1234567890123456";
    private final String INVALID_CREDIT_CARD_NUMBER = "";
    private final long NON_EXISTENT_EXCURSION_ID=-1;
    private final long NON_EXISTENT_RESERVA_ID=-1;
    private final String USER_MAIL = "ws@test.com";
    private final short VALID_RESERVA_NUMPLAZAS=1;
    private final short INVALID_RESERVA_NUMPLAZAS=0;
    private final float VALID_PRICE = 150;


    private static ExcursionService excursionService = null;
    private static SqlReservaDao reservaDao = null;
    private static SqlExcursionDao excursionDao = null;


    @BeforeAll
    public static void init() {

        DataSource dataSource = new SimpleDataSource();
        /* Add "dataSource" to "DataSourceLocator". */
        DataSourceLocator.addDataSource(APP_DATA_SOURCE, dataSource);
        reservaDao = SqlReservaDaoFactory.getDao();
        excursionService = ExcursionServiceFactory.getService();
        excursionDao = SqlExcursionDaoFactory.getDao();
    }

    private void removeExcursion(Long excursionId) {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()){
            try{
                /* Prepare connection. */
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                excursionDao.remove(connection, excursionId);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e) {
                connection.rollback();
                throw e;
            } catch (InstanceNotFoundException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private Excursion findExcursionById(Long excursionId) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);

        try (Connection connection = dataSource.getConnection()) {
            return excursionDao.findById(connection, excursionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private Excursion getValidExcursion(String ciudad) {
        return new Excursion(ciudad,"descripcion",LocalDateTime.now().plusHours(96).withNano(0),150F,(short) 15,(short) 4);
    }

    private Excursion createExcursion(Excursion excursion) {

        Excursion addedExcursion;
        try {
            addedExcursion = excursionService.addExcursion(excursion);
        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        }
        return addedExcursion;
    }
    private Reserva findReservaByID(long idReserva) throws InstanceNotFoundException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try(Connection connection =dataSource.getConnection()){
            return reservaDao.findById(connection,idReserva);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Reserva getValidReserva(long idExcursion){
        return new Reserva(USER_MAIL,idExcursion,VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER,VALID_PRICE);
    }
    private Reserva getValidReserva(long idReserva,long idExcursion){
        return new Reserva(idReserva,USER_MAIL,idExcursion,VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER,LocalDateTime.now().withNano(0),VALID_PRICE);
    }

    private void updateFechaExcursion(long idExcursion, LocalDateTime fecha) throws InputValidationException {
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try(Connection connection  = dataSource.getConnection()) {
            try {
                Excursion excursion = excursionDao.findById(connection,idExcursion);
                excursion.setFechaCelebracion(fecha);
                //Prepare connection
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);
                //Do work
                excursionDao.update(connection,excursion);
                //Commit
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException(e);
            } catch (RuntimeException | Error e){
                connection.rollback();
                throw e;
            } catch (InputValidationException e) {
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeReserva(long idReserva){
        DataSource dataSource = DataSourceLocator.getDataSource(APP_DATA_SOURCE);
        try(Connection connection  = dataSource.getConnection()) {
            try {
                //Prepare connection
                connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
                connection.setAutoCommit(false);

                //Do work
                reservaDao.remove(connection, idReserva);
                //Commit
                connection.commit();
            } catch (InstanceNotFoundException e) {
                connection.commit();
                throw new RuntimeException(e);
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

    @Test
    public void testAddExcursionAndFindExcursion() throws InstanceNotFoundException, InputValidationException {

        List <Excursion> excursionesCiudad1 = new LinkedList<>();
        List <Excursion> excursionesCiudad2 = new LinkedList<>();
        try {

            Excursion excursion1 = createExcursion(getValidExcursion("ciudad 1"));
            Excursion excursion2 = createExcursion(getValidExcursion("ciudad 2"));
            Excursion excursion3 = createExcursion(getValidExcursion("ciudad 2"));

            excursionesCiudad1.add(excursion1);
            excursionesCiudad2.add(excursion2);
            excursionesCiudad2.add(excursion3);

            List<Excursion> foundExcursionesCiudad1 = excursionService.findExcursion(excursion1.getCiudad(), null, null);
            List<Excursion> foundExcursionesCiudad2 = excursionService.findExcursion(excursion2.getCiudad(), null, null);

            assertEquals(excursionesCiudad1, foundExcursionesCiudad1);
            assertEquals(excursionesCiudad2, foundExcursionesCiudad2);

        } finally {
            // Clear Database
            for (Excursion excursion : excursionesCiudad1) {
                removeExcursion(excursion.getExcId());
            }
            for (Excursion excursion : excursionesCiudad2) {
                removeExcursion(excursion.getExcId());
            }
        }
    }

    @Test
    public void testFindExcursionWithDates() throws  InstanceNotFoundException, InputValidationException {

        List <Excursion> excursionesHora1 = new LinkedList<>();
        List <Excursion> excursionesHora2 = new LinkedList<>();
        try {
            // tengo dos excursiones iguales (en la misma ciudad, a la misma hora, el mismo dia)
            Excursion excursion1 = createExcursion(getValidExcursion("ciudad 1"));
            Excursion excursion2 = createExcursion(getValidExcursion("ciudad 1"));

            // y tengo una excursion que es 3 dias (72 horas) despues de ellas
            Excursion excursion3 = createExcursion(new Excursion("ciudad 1", "descripcion",
                    excursion1.getFechaCelebracion().plusHours(72),
                    200F, (short)15, (short)0));

            excursionesHora1.add(excursion1);
            excursionesHora1.add(excursion2);
            excursionesHora2.add(excursion3);

            // filtro para que coja las dos primeras excursiones pero no la segunda
            List<Excursion> foundExcursionesHora1 = excursionService.findExcursion(excursion1.getCiudad(),
                    excursion1.getFechaCelebracion().minusHours(24).withNano(0),
                    excursion1.getFechaCelebracion().plusHours(24).withNano(0));
            // filtro para que coja la tercera excursion pero no las dos primeras
            List<Excursion> foundExcursionesHora2 = excursionService.findExcursion(excursion2.getCiudad(),
                    excursion3.getFechaCelebracion().minusHours(24).withNano(0),
                    excursion3.getFechaCelebracion().plusHours(24).withNano(0));

            assertEquals(excursionesHora1, foundExcursionesHora1);
            assertEquals(excursionesHora2, foundExcursionesHora2);

        } finally {
            // Clear Database
            for (Excursion excursion : excursionesHora1) {
                removeExcursion(excursion.getExcId());
            }
            for (Excursion excursion : excursionesHora2) {
                removeExcursion(excursion.getExcId());
            }
        }
    }

    @Test
    public void testFechaFinalBeforeInicial(){
        Excursion excursion = createExcursion(getValidExcursion("ciudad 1"));
        assertThrows(InputValidationException.class, () -> excursionService.findExcursion("ciudad1",
                LocalDateTime.now().plusHours(24).withNano(0),
                LocalDateTime.now().withNano(0)));
        removeExcursion(excursion.getExcId());
    }


    @Test
    public void AddInvalidExcursion(){
        // check Excursion ciudad not null
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setCiudad(null);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion ciudad not empty
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setCiudad("");
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion descripcion not null
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setDescripcion(null);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion descripcion not empty
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setDescripcion("");
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion precio >= 0
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setPrecio(-1F);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion numMaxPers >= 0
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setNumMaxPers((short)-1);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

        // check Excursion numPers >= 0
        assertThrows(InputValidationException.class, () -> {
            Excursion excursion = getValidExcursion("ciudad");
            excursion.setNumPers((short)-1);
            Excursion addedExcursion = excursionService.addExcursion(excursion);
            removeExcursion(addedExcursion.getExcId());
        });

    }

    @Test
    public void testNotEnoughNotice() {
        Excursion excursion = getValidExcursion("ciudad");
        excursion.setFechaCelebracion(LocalDateTime.now().plusHours(48).withNano(0));

        assertThrows(InputValidationException.class, () -> excursionService.addExcursion(excursion));
    }

    @Test
    public void testFechaFinalIsBefore() {
        Excursion excursion = createExcursion(getValidExcursion("ciudad"));
        try{
            LocalDateTime fechaInicial = LocalDateTime.now().minusHours(48).withNano(0);
            LocalDateTime fechaFinal = LocalDateTime.now().minusHours(71).withNano(0);

            assertThrows(InputValidationException.class, () -> excursionService.findExcursion(excursion.getCiudad(), fechaInicial, fechaFinal));

        } finally {
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testUpdateNonExistentExcursion() {
        Excursion excursion = getValidExcursion("ciudad");
        excursion.setExcId(-1L);
        assertThrows(InstanceNotFoundException.class, () -> excursionService.updateExcursion(excursion));
    }
    
    @Test
    public void testUpdate() throws InstanceNotFoundException, InputValidationException,
            NotEnoughNoticeToUpdateException {
        Excursion excursion = createExcursion(getValidExcursion("ciudad 1"));
        try {
            Excursion excursionParaModificar = new Excursion(excursion.getExcId(), "ciudad 2", "descripcion", LocalDateTime.of(2022, 12, 31, 0, 0), 200F, (short) 15 , (short) 4);

            excursionService.updateExcursion(excursionParaModificar);
            Excursion excursionModificada = findExcursionById(excursion.getExcId());

            excursionParaModificar.setFechaCreacion(excursionModificada.getFechaCreacion());
            assertEquals(excursionModificada,excursionParaModificar);

        } finally {
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testUpdateEarlyExcursion() {
        Excursion excursion = createExcursion(getValidExcursion("ciudad 1"));
        try{
            updateFechaExcursion(excursion.getExcId(),LocalDateTime.now().withNano(0));
            excursion.setFechaCelebracion(LocalDateTime.now().withNano(0).plusHours(24));
            assertThrows(NotEnoughNoticeToUpdateException.class, () -> excursionService.updateExcursion(excursion));

        } catch (InputValidationException e) {
            throw new RuntimeException(e);
        } finally {
            removeExcursion(excursion.getExcId());
        }

    }

    @Test
    public void testEarlyCelebrationDateToUpdate(){
        Excursion excursion = createExcursion(getValidExcursion("ciudad 1"));
        try {
            excursion.setFechaCelebracion(excursion.getFechaCelebracion().minusHours(3));
            assertThrows(InputValidationException.class, () -> excursionService.updateExcursion(excursion));
        } finally {
             removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testTooLowNumMaxPersToUpdate(){
        Excursion excursion = createExcursion(getValidExcursion("ciudad 1"));
        try {
            excursion.setNumMaxPers((short)1);
            assertThrows(InputValidationException.class, () -> excursionService.updateExcursion(excursion));
        } finally {
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testAddReservaAndFindReserva() throws InstanceNotFoundException, InputValidationException {
        Excursion excursion = createExcursion(getValidExcursion("ciudad"));
        long idReserva = -1;
        try {
            LocalDateTime beforeAddReserva= LocalDateTime.now().withNano(0);
            idReserva = excursionService.addReserva(USER_MAIL, excursion.getExcId(), VALID_RESERVA_NUMPLAZAS, VALID_CREDIT_CARD_NUMBER);
            LocalDateTime afterAddReserva = LocalDateTime.now().withNano(0);

            Reserva reserva = getValidReserva(idReserva,excursion.getExcId());
            Reserva result = findReservaByID(idReserva);

            assertEquals(reserva.getReservaId(),result.getReservaId());
            assertEquals(reserva.getEmail(),result.getEmail());
            assertEquals(reserva.getExcId(),result.getExcId());
            assertEquals(reserva.getNumPlazas(),result.getNumPlazas());
            assertEquals(reserva.getTarjetaCredito(),result.getTarjetaCredito());
            assertTrue((result.getFecha().compareTo(beforeAddReserva) >= 0)
                    && (result.getFecha().compareTo(afterAddReserva) <= 0));
            assertNull(result.getFechaCancelacion());
            assertEquals(VALID_PRICE,reserva.getPrecioCompra());
        } finally {
            //Clear database
            if (idReserva != -1)removeReserva(idReserva);
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testCancelReserva() throws InstanceNotFoundException, InputValidationException, TooLateToCancelReservaException {
        Excursion excursion = createExcursion(getValidExcursion("ciudad"));
        long idReserva = -1;
        try {
            idReserva = excursionService.addReserva(USER_MAIL, excursion.getExcId(), VALID_RESERVA_NUMPLAZAS, VALID_CREDIT_CARD_NUMBER);
            excursionService.cancelReserva(idReserva,USER_MAIL);
            Reserva result = findReservaByID(idReserva);
            assertNotNull(result.getFechaCancelacion());
        } finally {
            //Clear database
            if (idReserva != -1)removeReserva(idReserva);
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testFindReservaByUserMail() throws InstanceNotFoundException, InputValidationException {
        Excursion excursion = createExcursion(getValidExcursion("ciudad"));
        Reserva reserva = null;
        List<Reserva> listReservas = new ArrayList<>();
        try {
            reserva = getValidReserva(excursion.getExcId());

            long idReserva1 = excursionService.addReserva(reserva.getEmail(), excursion.getExcId(), reserva.getNumPlazas(), reserva.getTarjetaCredito());
            long idReserva2 = excursionService.addReserva(reserva.getEmail(), excursion.getExcId(), reserva.getNumPlazas(), reserva.getTarjetaCredito());
            long idReserva3 = excursionService.addReserva(reserva.getEmail(), excursion.getExcId(), reserva.getNumPlazas(), reserva.getTarjetaCredito());

            listReservas.add(findReservaByID(idReserva1));
            listReservas.add(findReservaByID(idReserva2));
            listReservas.add(findReservaByID(idReserva3));

            List<Reserva> result = excursionService.findReservaByUserMail(USER_MAIL);
            assertEquals(listReservas,result);
        } finally {
            //Clear database
            if (reserva != null) for (Reserva res : listReservas) removeReserva(res.getReservaId());
            removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testAddInvalidReserva() {
        Excursion excursion = null;
        try{
            try {
                excursion = excursionService.addExcursion(getValidExcursion("ciudad"));
            } catch (InputValidationException e) {
                throw new RuntimeException(e);
            }

            // check Reserva's ExcursionID not found
            assertThrows(InstanceNotFoundException.class, () -> excursionService.addReserva(USER_MAIL,NON_EXISTENT_EXCURSION_ID,VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER));

            // check Reserva's Numplazas not valid
            Excursion finalExcursion = excursion;
            assertThrows(InputValidationException.class, () -> excursionService.addReserva(USER_MAIL, finalExcursion.getExcId(),INVALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER));

            // check faltan menos de 24h para a excursion
            assertThrows(InputValidationException.class, () -> {
                updateFechaExcursion(finalExcursion.getExcId(),LocalDateTime.now().withNano(0).plusHours(10));
                excursionService.addReserva(USER_MAIL, finalExcursion.getExcId(),VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER);
            });

            // check invalid credit card number
            assertThrows(InputValidationException.class, () -> excursionService.addReserva(USER_MAIL, finalExcursion.getExcId(),VALID_RESERVA_NUMPLAZAS,INVALID_CREDIT_CARD_NUMBER));
        }finally {
            if (excursion != null) removeExcursion(excursion.getExcId());
        }

    }

    @Test
    public void testTooLateToCancel() {
        Excursion excursion = null;Long idreserva=null;
        try{
            excursion = getValidExcursion("ciudad");
            try {
                excursion = excursionService.addExcursion(excursion);
            } catch (InputValidationException e) {
                throw new RuntimeException(e);
            }
            idreserva = excursionService.addReserva(USER_MAIL, excursion.getExcId(),VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER);

            updateFechaExcursion(excursion.getExcId(),LocalDateTime.now().plusHours(23).withNano(0));

            Long finalIdreserva = idreserva;
            assertThrows(TooLateToCancelReservaException.class, () -> excursionService.cancelReserva(finalIdreserva,USER_MAIL));
        } catch (InstanceNotFoundException | InputValidationException e) {
            throw new RuntimeException(e);
        } finally {
            if (idreserva!=null)removeReserva(idreserva);
            if (excursion!=null)removeExcursion(excursion.getExcId());

        }

    }
    @Test
    public void testReservaAlreadyCancel() {
        Excursion excursion = null;
        long idReserva = -1;
        try{
            try {
                excursion = excursionService.addExcursion(getValidExcursion("ciudad"));
                idReserva = excursionService.addReserva(USER_MAIL, excursion.getExcId(),VALID_RESERVA_NUMPLAZAS,VALID_CREDIT_CARD_NUMBER);
                excursionService.cancelReserva(idReserva,USER_MAIL);
            } catch (InputValidationException | InstanceNotFoundException | TooLateToCancelReservaException e) {
                throw new RuntimeException(e);
            }
            long finalIdReserva = idReserva;
            assertThrows(InputValidationException.class, () -> excursionService.cancelReserva(finalIdReserva,USER_MAIL));
        }finally {
            if(idReserva!=-1) removeReserva(idReserva);
            if (excursion!=null) removeExcursion(excursion.getExcId());
        }
    }

    @Test
    public void testFindReservaOfWrongMail(){
        List<Reserva> listaVacia = new ArrayList<>();
        List<Reserva> result = excursionService.findReservaByUserMail("ws2@test.com");
        assertEquals(listaVacia,result);
    }
    @Test
    public void testFindReservaWrongId(){
        assertThrows(InstanceNotFoundException.class, () -> findReservaByID(NON_EXISTENT_RESERVA_ID));
    }

}


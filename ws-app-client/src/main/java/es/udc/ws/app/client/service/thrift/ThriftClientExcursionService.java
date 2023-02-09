package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservaDto;
import es.udc.ws.app.client.service.exceptions.ClientTooLateToCancelReservaException;
import es.udc.ws.app.thrift.ThriftExcursionService;
import es.udc.ws.app.thrift.ThriftInputValidationException;
import es.udc.ws.app.thrift.ThriftInstanceNotFoundException;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDate;
import java.util.List;

public class ThriftClientExcursionService implements ClientExcursionService {
    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientExcursionService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);


    @Override
    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException {

        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try{
            transport.open();

            return client.addExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion)).getExcId();
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            transport.close();
        }

    }

    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException, InstanceNotFoundException {
        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try{
            transport.open();
            client.updateExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion));
        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e){
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            transport.close();
        }
    }

    @Override
    public List<ClientExcursionDto> findExcursiones(LocalDate dateini, LocalDate datefin, String ciudad) throws InputValidationException {
        return null;
    }

    @Override
    public Long addReserva(String userMail, Long excursionId, String creditCardNumber, Short places) throws InstanceNotFoundException, InputValidationException {
        return null;
    }

    @Override
    public void cancelReserva(Long reserveId, String userMail) throws InstanceNotFoundException, ClientTooLateToCancelReservaException, InputValidationException {

    }

    @Override
    public List<ClientReservaDto> findReserva(String userMail) {
        return null;
    }

    private ThriftExcursionService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftExcursionService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }
}

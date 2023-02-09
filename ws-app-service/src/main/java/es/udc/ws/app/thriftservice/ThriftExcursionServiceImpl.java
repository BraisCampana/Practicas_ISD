package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.excursionservice.ExcursionServiceFactory;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public class ThriftExcursionServiceImpl implements ThriftExcursionService.Iface {

    @Override
    public ThriftExcursionDto addExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException {
        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try {
            Excursion excCreada = ExcursionServiceFactory.getService().addExcursion(excursion);
            return ExcursionToThriftExcursionDtoConversor.toThriftExcursionDto(excCreada);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void updateExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException, ThriftInstanceNotFoundException, ThriftNotEnoughNoticeToUpdateException {
        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try {
            ExcursionServiceFactory.getService().updateExcursion(excursion);
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.' + 1)));
        } catch (es.udc.ws.app.model.excursionservice.exceptions.NotEnoughNoticeToUpdateException e) {
            throw new ThriftNotEnoughNoticeToUpdateException(e.getMessage());
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }
}
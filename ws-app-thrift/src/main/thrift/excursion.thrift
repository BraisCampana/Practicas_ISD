namespace java es.udc.ws.app.thrift


struct ThriftExcursionDto {
    1: i64 excId;
    2: string ciudad;
    3: string descripcion;
    4: string fechaCelebracion;
    5: double precio;
    6: i32 numMaxPers;
    7: i32 numPers;
}

struct ThriftReservaDto {
    1: i64 reservaId;
    2: string email;
    3: i64 excId;
    4: i32 numPlazas;
    5: string last4digTarjeta;
    6: string fechaCreacion;
    7: string fechaCancelacion;
    8: double precioCompra;
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftNotEnoughNoticeToUpdateException {
    1: string message;
}



service ThriftExcursionService {
       ThriftExcursionDto addExcursion(1: ThriftExcursionDto excursionDto) throws (1: ThriftInputValidationException e)

       void updateExcursion (1: ThriftExcursionDto excursionDto) throws (1: ThriftInputValidationException e1, 2: ThriftInstanceNotFoundException e2,
       3: ThriftNotEnoughNoticeToUpdateException e3)
}
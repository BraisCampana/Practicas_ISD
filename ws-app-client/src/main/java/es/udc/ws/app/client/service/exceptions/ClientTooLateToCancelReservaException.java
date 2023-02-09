package es.udc.ws.app.client.service.exceptions;

public class ClientTooLateToCancelReservaException extends Exception{
    private Long reservaId;

    public ClientTooLateToCancelReservaException(Long reservaId) {
        super("It's too late to cancel this reserve id=\""+reservaId+"\"");
        this.reservaId = reservaId;
    }

    public Long getReservaId() {return reservaId;}
    public void setReservaId(Long reservaId) {this.reservaId = reservaId;}
}

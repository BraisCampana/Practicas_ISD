package es.udc.ws.app.model.excursionservice.exceptions;

public class TooLateToCancelReservaException extends Exception{
        private Long reservaId;

        public TooLateToCancelReservaException(Long reservaId) {
            super("It's too late to cancel this reserve nº "+reservaId);
            this.reservaId = reservaId;
        }

    public Long getReservaId() {return reservaId;}
    public void setReservaId(Long reservaId) {this.reservaId = reservaId;}
}

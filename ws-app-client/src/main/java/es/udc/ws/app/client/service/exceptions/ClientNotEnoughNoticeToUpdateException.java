package es.udc.ws.app.client.service.exceptions;

public class ClientNotEnoughNoticeToUpdateException extends Exception {
    private Long excId;

    public ClientNotEnoughNoticeToUpdateException(Long excId) {
        super("Excursion con Id " + excId +
                " tiene una fecha de Celebracion con poca antelación con respecto a la fecha actual. No se podrá actualizar. ");
        this.excId = excId;
    }

    public Long getExcId() {
        return excId;
    }

    public void setExcId(Long excId) {
        this.excId = excId;
    }
}


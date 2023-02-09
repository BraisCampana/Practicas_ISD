package es.udc.ws.app.model.excursionservice.exceptions;

public class NotEnoughNoticeToUpdateException extends Exception{
    private Long excId;

    public NotEnoughNoticeToUpdateException(Long excId) {
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

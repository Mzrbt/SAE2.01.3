package app.model.exceptions;

public class UnknownPlaceException extends RuntimeException {
    public UnknownPlaceException() {
    }

    public UnknownPlaceException(String message) {
        super(message);
    }
}

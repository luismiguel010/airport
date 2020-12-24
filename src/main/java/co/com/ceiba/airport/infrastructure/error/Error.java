package co.com.ceiba.airport.infrastructure.error;

public class Error {

    private String nameException;
    private String message;

    public Error(String nameException, String message) {
        this.nameException = nameException;
        this.message = message;
    }

    public String getNameException() {
        return nameException;
    }

    public String getMessage() {
        return message;
    }
}

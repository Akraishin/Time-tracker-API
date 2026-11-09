package training.restapi.demo1.exceptions;


public class ConflictException extends RuntimeException{
    public ConflictException(String errorMessage) {
        super(errorMessage);
    }
}

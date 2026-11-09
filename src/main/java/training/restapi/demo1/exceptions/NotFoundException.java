package training.restapi.demo1.exceptions;

public class NotFoundException extends RuntimeException{

    public NotFoundException(String errorMessage) {
        super(errorMessage);
    }
}

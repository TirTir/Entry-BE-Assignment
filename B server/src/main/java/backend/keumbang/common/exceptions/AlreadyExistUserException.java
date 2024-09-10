package backend.keumbang.common.exceptions;

public class AlreadyExistUserException extends RuntimeException {

    public AlreadyExistUserException(String message) {
        super(message);
    }
}
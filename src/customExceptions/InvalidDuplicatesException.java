package customExceptions;

public class InvalidDuplicatesException extends IllegalArgumentException {
    public InvalidDuplicatesException(String msg){
        super(msg);
    }
}

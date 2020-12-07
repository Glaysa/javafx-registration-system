package customExceptions;

public class InvalidBirthDateException extends IllegalArgumentException {
    public InvalidBirthDateException(String msg){ super(msg); }
}

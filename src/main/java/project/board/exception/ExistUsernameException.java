package project.board.exception;

public class ExistUsernameException extends RuntimeException{
    public ExistUsernameException() {
        super("이미 존재하는 ID입니다.");
    }

    public ExistUsernameException(String message) {
        super(message);
    }

    public ExistUsernameException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistUsernameException(Throwable cause) {
        super(cause);
    }

    protected ExistUsernameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

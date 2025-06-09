package project.board.common.exception;

public class ExistEmailException extends RuntimeException{
    public ExistEmailException() {
        super("이미 존재하는 이메일입니다.");
    }

    public ExistEmailException(String message) {
        super(message);
    }

    public ExistEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExistEmailException(Throwable cause) {
        super(cause);
    }

    protected ExistEmailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

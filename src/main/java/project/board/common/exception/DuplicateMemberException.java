package project.board.common.exception;

public class DuplicateMemberException extends RuntimeException{
    public DuplicateMemberException() {
        super("이미 존재하는 이메일 또는 아이디입니다.");
    }

    public DuplicateMemberException(String message) {
        super(message);
    }

    public DuplicateMemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateMemberException(Throwable cause) {
        super(cause);
    }

    public DuplicateMemberException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

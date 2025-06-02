package project.board.exception;

public class MissMatchOldPassword extends RuntimeException{
    public MissMatchOldPassword() {
        super("기존의 비밀번호와 일치하지 않습니다.");
    }

    public MissMatchOldPassword(String message) {
        super(message);
    }

    public MissMatchOldPassword(String message, Throwable cause) {
        super(message, cause);
    }

    public MissMatchOldPassword(Throwable cause) {
        super(cause);
    }

    protected MissMatchOldPassword(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

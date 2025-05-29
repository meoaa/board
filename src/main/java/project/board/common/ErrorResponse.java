package project.board.common;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private boolean success;
    private int code;
    private String message;

    public ErrorResponse(int code, String message) {
        this.success = false;
        this.code = code;
        this.message = message;
    }
    public static ErrorResponse of(int code, String message){
        return new ErrorResponse(code, message);
    }
}

package project.board.common;

import lombok.Getter;

@Getter
public class ApiResponse <T>{
    private boolean success;
    private int code;
    private String message;
    private T data;

    private ApiResponse(int code, String message, T data) {
        this.success = true;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public static <T> ApiResponse<T> of(int code, String message, T data){
        return new ApiResponse<T>(code, message, data);
    }
}

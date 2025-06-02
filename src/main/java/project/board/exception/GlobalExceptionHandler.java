package project.board.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.board.common.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateMemberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicate(DuplicateMemberException ex){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ErrorResponse.of(408, ex.getMessage()));
    }

    @ExceptionHandler(ExistEmailException.class)
    public ResponseEntity<ErrorResponse> handleExistEmail(ExistEmailException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(409, ex.getMessage()));
    }

    @ExceptionHandler(ExistUsernameException.class)
    public ResponseEntity<ErrorResponse> handleExistUsername(ExistUsernameException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(410, ex.getMessage()));
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundMemberById(MemberNotFoundException ex){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(411, ex.getMessage()));
    }

    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ErrorResponse> handlePasswordMisMatch(PasswordMismatchException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(412, ex.getMessage()));
    }

    @ExceptionHandler(MissMatchOldPassword.class)
    public ResponseEntity<ErrorResponse> handleOldPasswordMisMatch(MissMatchOldPassword ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(412, ex.getMessage()));
    }
}

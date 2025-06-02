package project.board.dto.member;

import lombok.Getter;
import project.board.exception.PasswordMismatchException;

@Getter
public class PasswordChangeRequestDto {

    private String oldPassword;
    private String newPassword;
    private String newPasswordConfirm;

    public void validatePasswordMatch(){
        if(!newPassword.equals(newPasswordConfirm)){
            throw new PasswordMismatchException();
        }
    }
}

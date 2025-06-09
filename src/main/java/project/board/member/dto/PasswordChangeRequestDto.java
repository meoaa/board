package project.board.member.dto;

import lombok.Data;
import project.board.common.exception.PasswordMismatchException;

@Data
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

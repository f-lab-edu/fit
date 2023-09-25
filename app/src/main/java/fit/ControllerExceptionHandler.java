package fit;

import fit.command.Signup;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Void> handleDuplicateEmailException() {
        //  추후 로그 입력 시, 변수 입력 -> DuplicateEmailException e
        return ResponseEntity.badRequest().build();
    }
}

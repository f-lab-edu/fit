package fit;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<Void> handleDuplicateEmailException() {
        return ResponseEntity.badRequest().build();
    }
}

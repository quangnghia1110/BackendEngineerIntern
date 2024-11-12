package filum.ai.BackendEngineerIntern.model.payload.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ErrorException.class)
    public ResponseEntity<Object> handleErrorException(ErrorException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatus());
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomAccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(CustomAccessDeniedException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", "Bạn không có quyền truy cập");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }
}


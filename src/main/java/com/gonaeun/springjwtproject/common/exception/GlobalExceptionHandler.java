package com.gonaeun.springjwtproject.common.exception;

import com.gonaeun.springjwtproject.common.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<?> handleCustom(CustomException ex) {
        var code = ex.getCode();
        return ResponseEntity.status(code.getStatus())
            .body(Map.of("error", new ErrorResponse(code.name(), ex.getMessage())));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDenied(AccessDeniedException ex) {
        var code = ErrorCode.ACCESS_DENIED;
        return ResponseEntity.status(code.getStatus())
            .body(Map.of("error", new ErrorResponse(code.name(), code.getDefaultMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        var msg = ex.getBindingResult().getAllErrors().stream()
            .findFirst().map(e -> e.getDefaultMessage()).orElse("요청이 올바르지 않습니다.");
        return ResponseEntity.badRequest()
            .body(Map.of("error", new ErrorResponse("BAD_REQUEST", msg)));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleEtc(Exception ex) {
        return ResponseEntity.internalServerError()
            .body(Map.of("error", new ErrorResponse("INTERNAL_SERVER_ERROR", ex.getMessage())));
    }
}

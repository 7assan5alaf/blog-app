package com.hks.blog.exception;

import com.hks.blog.response.ErrorResponse;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ErrorResponse> handleException(DisabledException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder().errorCode(ErrorCodes.ACCOUNT_DISABLED.getCode())
                        .errorDescription(ErrorCodes.ACCOUNT_DISABLED.getDescription())
                        .build());

    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleException(BadCredentialsException exp) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder().errorCode(ErrorCodes.BAD_CREDENTIALS.getCode())
                        .errorDescription(ErrorCodes.BAD_CREDENTIALS.getDescription())
                        .error("Invalid email or password").build());

    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorResponse> handleException(MessagingException exp) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.builder().error(exp.getMessage()).build());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException exp) {
        Set<String> errors = new HashSet<>();
        exp.getBindingResult().getAllErrors().forEach(e -> errors.add(e.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder().validationError(errors).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception exp) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder()
                .errorDescription("Internal error, please contact the admin").error(exp.getMessage()).build());
    }

    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ErrorResponse> handleException(EntityNotFound exp) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder()
                .error(exp.getMessage()).build());
    }
    @ExceptionHandler(OperationPermittedException.class)
    public ResponseEntity<ErrorResponse> handleException(OperationPermittedException exp) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder()
                .error(exp.getMessage()).build());
    }

}

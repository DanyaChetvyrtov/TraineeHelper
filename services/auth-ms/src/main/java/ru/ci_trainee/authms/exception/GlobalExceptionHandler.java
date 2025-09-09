package ru.ci_trainee.authms.exception;

import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.io.DecodingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.ci_trainee.authms.dto.response.ExceptionResponse;
import ru.ci_trainee.authms.exception.exception.PasswordsDoNotMatchException;
import ru.ci_trainee.authms.exception.exception.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e,
            HttpServletRequest request
    ) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();

        var exceptionResponse = ExceptionResponse.builder()
                .message("Validation failed")
                .status(400)
                .error("Bad request")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        exceptionResponse.setErrors(fieldErrors.stream().collect(
                Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)
        ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class,
            PasswordsDoNotMatchException.class,
            DecodingException.class,
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ExceptionResponse> handleBadRequestException(
            RuntimeException e,
            HttpServletRequest request
    ) {
        var exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .status(400)
                .error("Bad request")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler({
            SignatureException.class
    })
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ExceptionResponse> handleUnauthorizedException(
            RuntimeException e,
            HttpServletRequest request
    ) {
        var exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .status(401)
                .error("Unauthorized")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exceptionResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ExceptionResponse> handleAccountNotFoundException(
            Exception e,
            HttpServletRequest request
    ) {
        var exceptionResponse = ExceptionResponse.builder()
                .message(e.getMessage())
                .status(404)
                .error("Not found")
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exceptionResponse);
    }
}

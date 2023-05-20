package com.java.advisor;

import com.java.exception.APIException;
import com.java.response.APIResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.NonNull;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class APIControllerAdvisor extends ResponseEntityExceptionHandler {

    @ExceptionHandler(APIException.class)
    public ResponseEntity<?> handleAPIException(APIException e, WebRequest request) {
        logger.error(e);
        return ResponseEntity.status(e.getHttpStatus())
                .body(APIResponse.failed(e.getHttpStatus().value(), null,
                        e.getMessage()));
    }

    @ExceptionHandler(value = JpaSystemException.class)
    public ResponseEntity<APIResponse<?>> handleJpaSystemException(JpaSystemException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), null, e.getCause().getCause().toString()));
    }

    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        String message = ex.getMessage();
        logger.error(message);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(APIResponse.failed(HttpStatus.BAD_REQUEST.value(), errors, null));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(APIResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), null,
                        exception.getMessage()));
    }

    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, @NonNull HttpHeaders headers,
            HttpStatus status, @NotNull WebRequest request) {
        return ResponseEntity.ok(APIResponse.failed(status.value(), null, ex.getMessage()));
    }

}

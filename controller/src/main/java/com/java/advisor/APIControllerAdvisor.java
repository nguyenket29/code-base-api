package com.java.advisor;

import com.java.constant.ErrorConstants;
import com.java.exception.APIException;
import com.java.response.APIResponse;
import com.java.util.StringUtil;
import com.java.util.Translator;
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
import java.util.*;

@RestControllerAdvice
public class APIControllerAdvisor extends ResponseEntityExceptionHandler {
    private final Translator translator;

    public APIControllerAdvisor(Translator translator) {
        this.translator = translator;
    }

    @ExceptionHandler(APIException.class)
    public ResponseEntity<?> handleAPIException(APIException e) {
        logger.error(e);
        if (!StringUtil.isEmpty(e.getCode())) {
            if (e.getMessage() == null) {
                String message = this.translator.getMessage(e.getCode());
                if (!e.getData().isEmpty()) {
                    message = StringUtil.format(message, e.getData());
                }
                e.setMessage(message);
            }
        }

        if (e.getErrors() != null) {
            List<Map<String, String>> localizeErrors = new ArrayList<>();
            for (Map<String, String> item : e.getErrors()) {
                if (item.containsKey("code")) {
                    item.put("message", translator.getMessage(item.get("code")));
                }
                localizeErrors.add(item);
            }
            e.setErrors(localizeErrors);
        }
        return ResponseEntity.status(e.getHttpStatus()).body(APIResponse.failed(e.getCode(), e.getMessage(), e.getErrors()));
    }

    @ExceptionHandler(value = JpaSystemException.class)
    public ResponseEntity<APIResponse<?>> handleJpaSystemException(JpaSystemException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.failed(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getCause().getCause().toString()));
    }

    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @NonNull HttpHeaders headers, @NonNull HttpStatus status, @NonNull WebRequest request) {
        String message = ex.getMessage();
        logger.error(message);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(APIResponse.failed(HttpStatus.BAD_REQUEST.toString(), null, Collections.singletonList(errors)));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<APIResponse<?>> handleException(Exception exception) {
        String accessDeniedMessage = "Access is denied";
        if (accessDeniedMessage.equals(exception.getMessage())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(APIResponse.failed(HttpStatus.FORBIDDEN.toString(), ErrorConstants.FORBIDDEN, null));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(APIResponse.failed(null, exception.getMessage(), null));
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, @NonNull HttpHeaders headers, HttpStatus status, @NotNull WebRequest request) {
        return ResponseEntity.ok(APIResponse.failed(status.value(), ex.getMessage()));
    }
}

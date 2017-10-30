package com.github.darrmirr.controller;

import com.github.darrmirr.exceptions.FieldValidationException;
import com.github.darrmirr.exceptions.UserNotFoundException;
import com.github.darrmirr.response.errors.ErrorResponseBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

/*
 * @author Darr Mirr
 */

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {UserNotFoundException.class})
    public ResponseEntity<Object> handleUserNotFoundException(UserNotFoundException ex) {
        return getResponseEntity(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(value = {IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
        return getResponseEntity(BAD_REQUEST, ex);
    }

    @ExceptionHandler(value = {FieldValidationException.class})
    public ResponseEntity<Object> handleFieldValidationException(FieldValidationException ex) {
        ErrorResponseBody errorResponseBody = getErrorValidation(ex.getResult());
        return ResponseEntity.status(BAD_REQUEST).body(errorResponseBody);
    }

    private ErrorResponseBody getErrorValidation(BindingResult result) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        result.getFieldErrors()
                .forEach(fieldError -> errorResponseBody.addError(FieldValidationException.class.getSimpleName(), fieldError.getField() + " " + fieldError.getDefaultMessage()));
        return errorResponseBody;
    }

    @ExceptionHandler(value = {RuntimeException.class})
    public ResponseEntity<Object> handleServerError(RuntimeException ex) {
        return getResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<Object> getResponseEntity(HttpStatus httpStatus, RuntimeException ex) {
        return ResponseEntity.status(httpStatus).body(getErrorResponseBody(ex));
    }

    private ErrorResponseBody getErrorResponseBody(Exception ex) {
        ErrorResponseBody errorResponseBody = new ErrorResponseBody();
        errorResponseBody.addError(ex.getClass().getSimpleName(), ex.getMessage());
        return errorResponseBody;
    }
}

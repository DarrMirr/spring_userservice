package com.github.darrmirr.exceptions;

import org.springframework.validation.BindingResult;

/*
 * @author Darr Mirr
 */

public class FieldValidationException extends RuntimeException {

    private BindingResult result;

    public FieldValidationException(BindingResult result) {
        this.result = result;
    }

    public BindingResult getResult() {
        return result;
    }
}

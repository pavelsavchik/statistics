package me.savchik.statistics.exception;

import java.util.List;

class ExceptionResponse {

    private List<ErrorMessage> errors;

    private List<ValidationError> fieldErrors;

    ExceptionResponse(List<ErrorMessage> errors, List<ValidationError> fieldErrors) {
        this.errors = errors;
        this.fieldErrors = fieldErrors;
    }

    public List<ErrorMessage> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }

    public List<ValidationError> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<ValidationError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
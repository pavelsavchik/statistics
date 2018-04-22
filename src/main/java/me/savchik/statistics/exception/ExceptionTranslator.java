package me.savchik.statistics.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import javax.validation.ConstraintViolationException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@ControllerAdvice
class ExceptionTranslator {

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            HttpMediaTypeNotSupportedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionResponse processRequestException(Exception exception) {
        List errors = Collections.singletonList(new ErrorMessage(exception.getMessage()));
        return new ExceptionResponse(errors, null);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionResponse processHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        List<ErrorMessage> errors = Collections.singletonList(new ErrorMessage("Invalid json"));
        return new ExceptionResponse(errors, null);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionResponse processMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        List<ValidationError> validationErrors = fieldErrors.stream()
                .map((FieldError error) -> new ValidationError(error.getDefaultMessage(), error.getField()))
                .collect(Collectors.toList());

        return new ExceptionResponse(null, validationErrors);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    ExceptionResponse processConstraintViolationException(ConstraintViolationException exception) {
        List fieldErrors = exception.getConstraintViolations().stream()
                .map(violation -> new ValidationError(violation.getMessage(), violation.getPropertyPath().toString()))
                .collect(Collectors.toList());
        return new ExceptionResponse(null, fieldErrors);
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity processUnknownException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

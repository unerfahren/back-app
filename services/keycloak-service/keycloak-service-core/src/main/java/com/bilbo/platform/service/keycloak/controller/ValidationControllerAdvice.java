package com.bilbo.platform.service.keycloak.controller;

import com.bilbo.platform.service.keycloak.model.ErrorDto;
import com.bilbo.platform.service.keycloak.model.ValidationErrorsDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ValidationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorsDto> handleValidationException(final MethodArgumentNotValidException exception) {

        final var errorsDto = new ValidationErrorsDto();

        exception.getBindingResult()
                .getAllErrors()
                .forEach(objectError -> errorsDto.addErrorsItem(
                        createErrorDto(objectError.getCode(), objectError.getDefaultMessage())));

        return ResponseEntity
                .badRequest()
                .body(errorsDto);
    }

    private ErrorDto createErrorDto(final String field,
                                    final String description) {

        return new ErrorDto()
                .field(field)
                .message(description);
    }
}
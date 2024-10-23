package com.bilbo.platfrom.service.company.controller;

import com.bilbo.platfrom.service.company.model.CommonErrorDto;
import com.bilbo.platfrom.service.company.model.ErrorDto;
import com.bilbo.platfrom.service.company.model.ValidationErrorsDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
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

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CommonErrorDto> handleNotFoundException(final EntityNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new CommonErrorDto().description(exception.getMessage()));
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<CommonErrorDto> handleOptimisticLock(final OptimisticLockingFailureException exception) {
        log.warn("Locked update", exception);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new CommonErrorDto().description("Update conflict, reload data"));
    }

    private ErrorDto createErrorDto(final String field,
                                    final String description) {

        return new ErrorDto()
                .field(field)
                .message(description);
    }
}
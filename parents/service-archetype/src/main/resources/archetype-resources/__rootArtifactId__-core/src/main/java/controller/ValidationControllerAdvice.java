#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import ${package}.model.ErrorDto;
import ${package}.model.ValidationErrorsDto;
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
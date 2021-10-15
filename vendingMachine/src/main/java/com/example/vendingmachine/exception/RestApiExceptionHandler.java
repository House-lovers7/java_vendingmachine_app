package com.example.vendingmachine.exception;

import com.example.vendingmachine.dto.ErrorDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

import static com.example.vendingmachine.util.RestValidationUtil.convertToErrorDto;

/**
 * 例外ハンドリングを行う {@link RestApiExceptionHandler}
 */
@RestControllerAdvice
public final class RestApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ServiceValidationException.class)
    public ResponseEntity handleException(ServiceValidationException exception) {
        return ResponseEntity.badRequest().body(getErrors(exception.getMessageCode(), exception.getArgs()));
    }

    private List<ErrorDto> getErrors(final String code, final String[] args) {
        final List<ErrorDto> errorList = new ArrayList<>();
        errorList.add(convertToErrorDto(messageSource, code, args));
        return errorList;
    }
}

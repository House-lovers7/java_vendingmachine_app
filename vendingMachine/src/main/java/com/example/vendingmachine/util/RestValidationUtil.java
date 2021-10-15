package com.example.vendingmachine.util;

import com.example.vendingmachine.dto.ErrorDto;
import org.springframework.context.MessageSource;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RestValidationUtil {

    private RestValidationUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * {@link BindingResult} に検証エラー情報がある場合に、検証エラー情報を {@link ErrorDto} の {@link List} の形式に変換して返します。
     *
     * @param messageSource {@link MessageSource}
     * @param bindingResult {@link BindingResult}
     * @return {@link List<ErrorDto>} を返します。検証エラーがない場合は、<code>null</code> を返します。
     * @throws IllegalStateException {@link BindingResult#hasErrors()} が <code>true</code> の場合で {@link FieldError} がない場合に発生します。
     */
    public static List<ErrorDto> getFieldValidationErrors(final MessageSource messageSource, final BindingResult bindingResult) {
        final List<ErrorDto> errorList = new ArrayList<>();
        if (bindingResult.hasErrors()) {
            for (final FieldError fieldError : bindingResult.getFieldErrors()) {
                errorList.add(convertToErrorDto(messageSource, fieldError.getDefaultMessage(), fieldError.getArguments()));
            }
            if (CollectionUtils.isEmpty(errorList)) {
                throw new IllegalStateException("Field error not found!!");
            }
            return errorList;
        }
        return Collections.emptyList();
    }

    public static ErrorDto convertToErrorDto(final MessageSource messageSource, final String code, final Object[] args) {
        return new ErrorDto(code, messageSource.getMessage(code, args, null));
    }
}

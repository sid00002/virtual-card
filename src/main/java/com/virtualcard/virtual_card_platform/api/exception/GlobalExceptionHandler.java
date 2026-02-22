package com.virtualcard.virtual_card_platform.api.exception;

import com.virtualcard.virtual_card_platform.common.response.ApiResponse;
import com.virtualcard.virtual_card_platform.common.response.ErrorResponse;
import com.virtualcard.virtual_card_platform.domain.exception.DomainException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleDomainException(DomainException ex) {

        ErrorResponse error =
                new ErrorResponse(ex.getCode(), ex.getMessage());

        return ApiResponse.failure(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleValidationException(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldError()
                .getDefaultMessage();

        ErrorResponse error =
                new ErrorResponse("VALIDATION_ERROR", message);

        return ApiResponse.failure(error);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneric(Exception ex) {

        ErrorResponse error =
                new ErrorResponse("INTERNAL_ERROR", ex.getMessage());

        return ApiResponse.failure(error);
    }
}
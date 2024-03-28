package com.whatpl.global.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("handleException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.GLOBAL_EXCEPTION);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(BizException.class)
    public ResponseEntity<ErrorResponse> handleBizException(BizException e) {
        log.error("handleBizException", e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.error("handleMissingServletRequestParameterException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.MISSING_PARAMETER);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error("handleMaxUploadSizeExceededException", e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.FILE_SIZE_EXCEED);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFoundException(NoHandlerFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.NOT_FOUND_API);
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(","));
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("CONSTRAINT")
                .message(message)
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DetailErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.REQUEST_VALUE_INVALID);
        List<DetailErrorResponse.DetailError> detailErrors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> DetailErrorResponse.DetailError.builder()
                        .field(error.getField())
                        .value(error.getRejectedValue())
                        .reason(error.getDefaultMessage())
                        .build())
                .toList();

        return new ResponseEntity<>(new DetailErrorResponse(errorResponse, detailErrors), errorResponse.getStatus());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormatException(HttpMessageNotReadableException e) {
        if (!(e.getRootCause() instanceof BizException bizException)) {
            return handleException(e);
        }
        return handleBizException(bizException);
    }
}
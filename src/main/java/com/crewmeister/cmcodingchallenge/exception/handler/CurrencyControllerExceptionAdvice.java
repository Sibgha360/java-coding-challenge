package com.crewmeister.cmcodingchallenge.exception.handler;

import com.crewmeister.cmcodingchallenge.exception.BadRequestException;
import com.crewmeister.cmcodingchallenge.exception.ExceptionResponse;
import com.crewmeister.cmcodingchallenge.exception.ResourceNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CurrencyControllerExceptionAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({BadRequestException.class, MethodArgumentTypeMismatchException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody
    ExceptionResponse handleBadRequestException(final Exception exception,
                                                final HttpServletRequest request) {

        logger.error(exception.getMessage(), exception);

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());

        return error;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public @ResponseBody
    ExceptionResponse handleNotFoundException(final Exception exception,
                                      final HttpServletRequest request) {
        logger.error(exception.getMessage(), exception);

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());

        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody
    ExceptionResponse handleException(final Exception exception,
                                      final HttpServletRequest request) {

        logger.error(exception.getMessage(), exception);

        ExceptionResponse error = new ExceptionResponse();
        error.setErrorMessage(exception.getMessage());
        error.callerURL(request.getRequestURI());

        return error;
    }
}
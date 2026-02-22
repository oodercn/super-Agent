package net.ooder.nexus.config;

import net.ooder.config.ResultModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResultModel<Void> handleNotFound(NoSuchElementException e) {
        log.warn("Resource not found: {}", e.getMessage());
        ResultModel<Void> result = new ResultModel<Void>();
        result.setRequestStatus(404);
        result.setMessage(e.getMessage());
        return result;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResultModel<Void> handleBadRequest(IllegalArgumentException e) {
        log.warn("Bad request: {}", e.getMessage());
        ResultModel<Void> result = new ResultModel<Void>();
        result.setRequestStatus(400);
        result.setMessage(e.getMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResultModel<Void> handleGenericError(Exception e) {
        log.error("Unexpected error", e);
        ResultModel<Void> result = new ResultModel<Void>();
        result.setRequestStatus(500);
        result.setMessage("Internal server error: " + e.getMessage());
        return result;
    }
}

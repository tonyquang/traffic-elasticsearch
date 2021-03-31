package com.traffic.monitor.exception;

import com.traffic.monitor.dto.ErrorRespone;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ElasticsearchQueryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorRespone elasticsearchQueryException(ElasticsearchQueryException ex, WebRequest webRequest){
        return ErrorRespone.builder()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .timestamp(new Date())
                .description(webRequest.getDescription(false)).build();
    }

}

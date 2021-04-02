package com.traffic.report.exception;

public class JwtException extends RuntimeException {
    public JwtException(String msg){
        super(msg);
    }

    public JwtException(String msg, Throwable t){
        super(msg, t);
    }
}

package com.traffic.report.exception;

public class AuthenticationException extends Exception{

    public AuthenticationException(String msg) {
        super(msg);
    }

    public AuthenticationException(String msg, Throwable t) {
        super(msg, t);
    }
}

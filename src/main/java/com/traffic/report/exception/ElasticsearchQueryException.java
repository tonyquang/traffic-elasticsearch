package com.traffic.report.exception;

public class ElasticsearchQueryException extends RuntimeException {

    public ElasticsearchQueryException(String msg) {
        super(msg);
    }

    public ElasticsearchQueryException(String msg, Throwable t) {
        super(msg, t);
    }
}

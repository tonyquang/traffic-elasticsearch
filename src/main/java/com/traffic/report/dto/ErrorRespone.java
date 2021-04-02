package com.traffic.report.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorRespone {

    private int statusCode;
    private String message;
    private Date timestamp;
    private String description;

}

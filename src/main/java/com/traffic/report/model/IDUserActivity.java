package com.traffic.report.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class IDUserActivity implements Serializable {
    private String userId;
    private String url;
    private String date;
}

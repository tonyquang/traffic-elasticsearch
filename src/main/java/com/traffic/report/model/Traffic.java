package com.traffic.report.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Traffic {

    @JsonProperty("url")
    private String url;

    @JsonProperty("localdate")
    private String timeStamp;
}

package com.traffic.report.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.traffic.report.model.Traffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficInfoResponse {

    @JsonProperty("user_id")
    private String userID;

    @JsonProperty("count")
    private int count;

    @JsonProperty("traffics")
    List<Traffic> traffics;
}

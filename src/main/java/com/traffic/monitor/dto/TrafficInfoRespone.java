package com.traffic.monitor.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.traffic.monitor.model.Traffic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrafficInfoRespone {

    @JsonProperty("user_id")
    private String userID;

    @JsonProperty("count")
    private int count;

    @JsonProperty("traffics")
    List<Traffic> traffics;
}

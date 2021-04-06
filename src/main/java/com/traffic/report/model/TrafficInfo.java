package com.traffic.report.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrafficInfo {

    @Id
    @JsonIgnoreProperties
    private String id;

    @JsonProperty("user_id")
    private String userid;

    @JsonProperty("url")
    private String url;

    @JsonProperty("localdate")
    private String localdate;

}

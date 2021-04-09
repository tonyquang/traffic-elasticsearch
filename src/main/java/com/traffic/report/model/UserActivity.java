package com.traffic.report.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_activity")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@IdClass(IDUserActivity.class)
public class UserActivity implements Serializable {

    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Id
    @Column(name = "url", nullable = false)
    private String url;

    @Column(name = "count")
    private Integer count;

    @Id
    @Column(name = "date", nullable = false)
    private String date;

    @Column(name = "total_time")
    private double totalTime;

}

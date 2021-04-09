package com.traffic.report.services;

import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.model.Traffic;
import com.traffic.report.model.UserActivity;

import java.util.List;
import java.util.Map;

public interface DatabaseTrafficService {
    // Query to database
    Map<String, List<UserActivity>> getAllTrafficGroupByDate(String userId, String hostName, String yearMonth);
}

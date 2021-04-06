package com.traffic.report.services;

import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;

import java.util.List;
import java.util.Map;

public interface TrafficService {
    TrafficInfoResponse getAllTrafficAnUser(String userID, String hostName, String fromDate, String toDate);
    Map<String, List<Traffic>> getAllTrafficAnUserGroupByHour(String userID, String hostName, String fromDate, String toDate) throws InvalidInputException;
}

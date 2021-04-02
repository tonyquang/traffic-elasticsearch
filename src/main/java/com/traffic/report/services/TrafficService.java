package com.traffic.report.services;

import com.traffic.report.dto.TrafficInfoRespone;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;

import java.util.List;
import java.util.Map;

public interface TrafficService {
    TrafficInfoRespone getAllTrafficAnUser(String userID, String hostName, String date);
    Map<String, List<Traffic>> getAllTrafficAnUserGroupByHour(String userID, String hostName, String date) throws InvalidInputException;
}

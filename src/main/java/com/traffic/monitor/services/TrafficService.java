package com.traffic.monitor.services;

import com.traffic.monitor.dto.TrafficInfoRespone;

public interface TrafficService {
    TrafficInfoRespone getAllTrafficAnUser(String userID, String hostName, String date);
}

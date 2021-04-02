package com.traffic.report.repository;

import com.traffic.report.model.TrafficInfo;
import java.util.List;

public interface TrafficInfoRepository {

    List<TrafficInfo> findTrafficInfo(String userId, String hostName,  String date);
}

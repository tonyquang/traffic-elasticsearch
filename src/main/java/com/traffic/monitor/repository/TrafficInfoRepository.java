package com.traffic.monitor.repository;

import com.traffic.monitor.model.TrafficInfo;
import java.util.List;

public interface TrafficInfoRepository {

    List<TrafficInfo> findByUserIDHostName(String userId, String hostName,  String date);

}

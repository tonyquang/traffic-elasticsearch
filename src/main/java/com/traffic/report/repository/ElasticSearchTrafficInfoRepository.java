package com.traffic.report.repository;

import com.traffic.report.model.Traffic;
import java.util.List;

public interface ElasticSearchTrafficInfoRepository {
    List<Traffic> findTrafficInfo(String userId, String hostName, String fromDate, String toDate);
}

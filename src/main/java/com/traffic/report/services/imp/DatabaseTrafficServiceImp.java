package com.traffic.report.services.imp;

import com.traffic.report.model.UserActivity;
import com.traffic.report.repository.DatabaseTrafficInfoRepository;
import com.traffic.report.services.DatabaseTrafficService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DatabaseTrafficServiceImp implements DatabaseTrafficService {
    @Autowired
    DatabaseTrafficInfoRepository databaseTrafficInfoRepository;
    @Override
    public Map<String, List<UserActivity>> getAllTrafficGroupByDate(String userId, String hostName, String yearMonth) {
        Map<String, List<UserActivity>> trafficGroupByDate = new HashMap<>();
        List<UserActivity> userActivityList = new ArrayList<>();
        userActivityList = databaseTrafficInfoRepository.selectUserActivityBelongToFilter(userId, hostName, yearMonth);
        if (userActivityList.isEmpty())
            return trafficGroupByDate;
        trafficGroupByDate = userActivityList.stream().collect(Collectors.groupingBy(ua -> {
            String strDateTime = ua.getDate();
            int index = strDateTime.lastIndexOf("-") + 1;
            return strDateTime.substring(index, index + 2);
        }));
        return trafficGroupByDate;
    }

}

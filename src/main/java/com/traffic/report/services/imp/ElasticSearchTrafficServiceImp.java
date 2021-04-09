package com.traffic.report.services.imp;

import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.exception.ElasticsearchQueryException;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;
import com.traffic.report.repository.ElasticSearchTrafficInfoRepository;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.TimeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElasticSearchTrafficServiceImp implements TrafficService {

    @Autowired
    ElasticSearchTrafficInfoRepository elasticSearchTrafficInfoRepository;

    @Autowired
    TimeUtil timeUtil;

    @Override
    public TrafficInfoResponse getAllTrafficAnUser(String userID, String hostName, String fromDate, String toDate) {
        List<Traffic> traffics;
        try {
            traffics = elasticSearchTrafficInfoRepository.findTrafficInfo(userID, hostName, fromDate, toDate);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ElasticsearchQueryException("Execute query to Elasticsearch error " + ex.getMessage(), ex);
        }
        if (traffics.isEmpty())
            return TrafficInfoResponse.builder()
                    .traffics(new ArrayList<>())
                    .count(0)
                    .userID(userID)
                    .build();

        TrafficInfoResponse trafficInfoResponse = new TrafficInfoResponse();
        trafficInfoResponse.setUserID(userID);
        trafficInfoResponse.setTraffics(traffics);
        trafficInfoResponse.setCount(traffics.size());

        return trafficInfoResponse;
    }

    @Override
    public Map<String, List<Traffic>> getAllTrafficAnUserGroupByHour(String userID, String hostName,  String fromDate, String toDate) throws InvalidInputException {
        TrafficInfoResponse trafficInfoResponse = getAllTrafficAnUser(userID, hostName, fromDate, toDate);
        if (trafficInfoResponse == null) return null;
        List<Traffic> trafficList = trafficInfoResponse.getTraffics();
        Map<String, List<Traffic>> trafficInfoGroupByDate =
                trafficList.stream().collect(Collectors.groupingBy(traffic -> {
                    String strDateTime = traffic.getTimeStamp();
                    return timeUtil.getHour(strDateTime);
                }));
        return trafficInfoGroupByDate;
    }



}

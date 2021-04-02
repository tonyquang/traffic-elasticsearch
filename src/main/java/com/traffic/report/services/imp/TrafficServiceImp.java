package com.traffic.report.services.imp;

import com.traffic.report.dto.TrafficInfoRespone;
import com.traffic.report.exception.ElasticsearchQueryException;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;
import com.traffic.report.model.TrafficInfo;
import com.traffic.report.repository.TrafficInfoRepository;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TrafficServiceImp implements TrafficService {

    @Autowired
    TrafficInfoRepository trafficInfoRepository;

    @Autowired
    TimeUtil timeUtil;

    @Override
    public TrafficInfoRespone getAllTrafficAnUser(String userID, String hostName, String date) {
        List<TrafficInfo> trafficsInfo;
        try {
            trafficsInfo = trafficInfoRepository.findTrafficInfo(userID, hostName, date);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new ElasticsearchQueryException("Execute query to Elasticsearch error " + ex.getMessage(), ex);
        }
        if (trafficsInfo.isEmpty())
            return TrafficInfoRespone.builder()
                    .traffics(new ArrayList<>())
                    .count(0)
                    .userID(userID)
                    .build();
        TrafficInfoRespone trafficInfoRespone = new TrafficInfoRespone();
        List<Traffic> trafficList = new ArrayList<>();
        trafficInfoRespone.setUserID(userID);
        for (TrafficInfo ti : trafficsInfo) {
            trafficList.add(Traffic.builder()
                    .url(ti.getUrl())
                    .timeStamp(ti.getLocaldate())
                    .build()
            );
        }
        trafficInfoRespone.setTraffics(trafficList);
        trafficInfoRespone.setCount(trafficList.size());
        return trafficInfoRespone;
    }

    @Override
    public Map<String, List<Traffic>> getAllTrafficAnUserGroupByHour(String userID, String hostName, String date) throws InvalidInputException {
        if(hostName.isEmpty())
            throw  new InvalidInputException("Host name request param must be not empty!");
        TrafficInfoRespone trafficInfoRespone = getAllTrafficAnUser(userID, hostName, date);
        if (trafficInfoRespone == null) return null;
        List<Traffic> trafficList = trafficInfoRespone.getTraffics();
        Map<String, List<Traffic>> trafficInfoGroupByDate =
                trafficList.stream().collect(Collectors.groupingBy(traffic -> {
                    String strDateTime = traffic.getTimeStamp();
                    return timeUtil.getHour(strDateTime);
                }));
        return trafficInfoGroupByDate;
    }



}

package com.traffic.monitor.services.imp;

import com.traffic.monitor.dto.TrafficInfoRespone;
import com.traffic.monitor.exception.ElasticsearchQueryException;
import com.traffic.monitor.model.Traffic;
import com.traffic.monitor.model.TrafficInfo;
import com.traffic.monitor.repository.TrafficInfoRepository;
import com.traffic.monitor.services.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrafficServiceImp implements TrafficService {

    @Autowired
    TrafficInfoRepository trafficInfoRepository;

    @Override
    public TrafficInfoRespone getAllTrafficAnUser(String userID, String hostName, String date) {
        List<TrafficInfo> trafficsInfo;
        try {
            trafficsInfo = trafficInfoRepository.findByUserIDHostName(userID, hostName, date);
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

}

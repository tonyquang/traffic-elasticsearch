package com.traffic.monitor.controller;

import com.traffic.monitor.dto.TrafficInfoRespone;
import com.traffic.monitor.services.TrafficService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class TrafficController {
    @Autowired
    TrafficService trafficService;

    @GetMapping("/traffic/{userid}")
    public TrafficInfoRespone getTraffic(@PathVariable(value = "userid") String userid,
                                         @RequestParam(value = "host") String hostName,
                                         @RequestParam(value = "date", defaultValue = "") String date){
        return trafficService.getAllTrafficAnUser(userid, hostName, date);
    }
}

package com.traffic.report.controller;

import com.traffic.report.dto.TrafficInfoRespone;
import com.traffic.report.services.TrafficService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class TrafficRestController {
    @Autowired
    TrafficService trafficService;

    @GetMapping("/traffic/{userid}")
    public TrafficInfoRespone getTraffic(@PathVariable(value = "userid") String userid,
                                         @RequestParam(value = "host", defaultValue = "") String hostName,
                                         @RequestParam(value = "date", defaultValue = "") String date){
        return trafficService.getAllTrafficAnUser(userid, hostName, date);
    }

}

package com.traffic.report.controller;

import com.traffic.report.dto.TrafficInfoRespone;
import com.traffic.report.model.Admin;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.HashUtil;
import com.traffic.report.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Slf4j
public class TrafficRestController {
    @Autowired
    TrafficService trafficService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HashUtil hashUtil;

    @GetMapping("/traffic/{userid}")
    public TrafficInfoRespone getTraffic(@PathVariable(value = "userid") String userid,
                                         @RequestParam(value = "host", defaultValue = "") String hostName,
                                         @RequestParam(value = "date", defaultValue = "") String date){
        return trafficService.getAllTrafficAnUser(userid, hostName, date);
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody Admin authRequest) throws Exception {
        try {
            System.out.println(hashUtil.getSha512FromText(authRequest.getPassword()));
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), hashUtil.getSha512FromText(authRequest.getPassword()))
            );
        } catch (Exception ex) {
            throw new Exception("inavalid username/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}

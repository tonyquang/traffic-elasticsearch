package com.traffic.report.controller;

import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.exception.AuthenticationException;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Admin;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.HashUtil;
import com.traffic.report.util.JwtUtil;
import com.traffic.report.util.TimeUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
@Slf4j
@Api( tags = "Traffic Rest API")
public class TrafficRestController {
    @Autowired
    TrafficService trafficService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private TimeUtil timeUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private HashUtil hashUtil;

    @GetMapping("/traffic/{userid}")
    public TrafficInfoResponse getTraffic(@PathVariable(value = "userid") @ApiParam(example = "PC-QUANPHAM$") String userid,
                                          @RequestParam(value = "host", defaultValue = "") @ApiParam(example = "facebook") String hostName,
                                          @RequestParam(value = "fromDate", defaultValue = "") @ApiParam(example = "2021-04-05") String fromDate,
                                          @RequestParam(value = "toDate", defaultValue = "") @ApiParam(example = "2021-04-06") String toDate ) throws InvalidInputException {
        if(!fromDate.isEmpty() && !toDate.isEmpty()){
            if(timeUtil.compareDate(fromDate, toDate) > 0)
                throw new InvalidInputException("From date must be before To date");
        }

        if(fromDate.isEmpty() && !toDate.isEmpty()){
            throw new InvalidInputException("You must input From date");
        }

        return trafficService.getAllTrafficAnUser(userid, hostName, fromDate, toDate);
    }

    @PostMapping("/authenticate")
    public String generateToken(@RequestBody Admin authRequest) throws AuthenticationException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUserName(), hashUtil.getSha512FromText(authRequest.getPassword()))
            );
        } catch (Exception ex) {
            throw new AuthenticationException("Invalid user/password");
        }
        return jwtUtil.generateToken(authRequest.getUserName());
    }
}

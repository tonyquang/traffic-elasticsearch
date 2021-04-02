package com.traffic.report.controller;

import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;
import com.traffic.report.services.TrafficExporterService;
import com.traffic.report.services.TrafficService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping("/")
@Slf4j
public class TrafficController {
    @Autowired
    TrafficService trafficService;

    @Autowired
    TrafficExporterService trafficExporterService;

    @GetMapping("/traffic/line-chart/{userid}")
    public String getLineChartTraffic(@PathVariable(value = "userid") String userid,
                                      @RequestParam(value = "host", defaultValue = "") String hostName,
                                      @RequestParam(value = "date", defaultValue = "") String date, Model model){
        try {
            Map<String, Integer> trafficMap = new LinkedHashMap<>();
            Map<String, List<Traffic>> trafficMapGroupedByHour = trafficService.getAllTrafficAnUserGroupByHour(userid, hostName, date);
            List<String> hourSorted = new ArrayList<>(trafficMapGroupedByHour.keySet());
            Collections.sort(hourSorted);
            for (int i = 7; i < 23; i++){
                String hour = String.valueOf(i);
                trafficMap.put(hour, trafficMapGroupedByHour.containsKey(hour) ?
                        trafficMapGroupedByHour.get(hour).size() : 0);
            }
            model.addAttribute("trafficMap", trafficMap);
            model.addAttribute("userid", userid);
                model.addAttribute("date", date.isEmpty() ? "All of time" : date);
            log.info(trafficMap.toString());
        } catch (InvalidInputException e) {
            log.error("getLineChartTraffic error", e);
        }
        return "lineChart";
    }

    @GetMapping("/traffic/export-excel/{userid}")
    public void getCSVTrafficReport(@PathVariable(value = "userid") String userid,
                                    @RequestParam(value = "host", defaultValue = "") String hostName,
                                    @RequestParam(value = "date", defaultValue = "") String date, HttpServletResponse response){
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        userid = removeIllegalCharacterInFileName(userid);
        hostName = removeIllegalCharacterInFileName(hostName);
        date = removeIllegalCharacterInFileName(date);

        String headerKey = "Content-Disposition";
        String fName = userid+"_";
        if(!hostName.isEmpty())
            fName += hostName + "_";
        fName+=currentDateTime;
        String headerValue = String.format("attachment; filename=%s.xlsx",fName);
        response.setHeader(headerKey, headerValue);

        trafficExporterService.exportCSV(response, userid, hostName, date);
    }

    private String removeIllegalCharacterInFileName(String fName){
        return fName.replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}

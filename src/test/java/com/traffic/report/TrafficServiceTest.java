package com.traffic.report;

import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;
import com.traffic.report.model.TrafficInfo;
import com.traffic.report.repository.TrafficInfoRepository;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.TimeUtil;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
public class TrafficServiceTest {

    @Autowired
    TrafficService trafficService;

    @Autowired
    TimeUtil timeUtil;

    @MockBean
    TrafficInfoRepository trafficInfoRepository;

    String host = "facebook";
    String userid = "quang.bui";
    String date = "2021-04-02";
    String toDate = "2021-04-05";

    TrafficInfoResponse expectedResponseGetAllTrafficAnUser;
    List<TrafficInfo> trafficsInfo;

    Map<String, List<Traffic>> expectedResponseGetAllTrafficAnUserGroupByHour;

    @Before
    public void setup() {
        trafficsInfo = new ArrayList<>();
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT edge-chat.facebook.com:443 HTTP/1.1").localdate(date+"T08:47:25.139+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT www.facebook.com:443 HTTP/1.1").localdate(date+"T08:48:24.039+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT CONNECT graph.facebook.com:443 HTTP/1.1").localdate(date+"T08:57:11.630+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT www.facebook.com:443 HTTP/1.1").localdate(date+"T09:00:39.431+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT www.facebook.com:443 HTTP/1.1").localdate(date+"T09:00:39.431+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT www.facebook.com:443 HTTP/1.1").localdate(date+"T10:00:39.431+0700").build());
        trafficsInfo.add(TrafficInfo.builder().userid(userid).url("CONNECT www.facebook.com:443 HTTP/1.1").localdate(date+"T10:00:39.431+0700").build());

        List<Traffic> traffics = new ArrayList<>();
        for (TrafficInfo ti : trafficsInfo) {
            traffics.add(Traffic.builder()
                    .url(ti.getUrl())
                    .timeStamp(ti.getLocaldate())
                    .build()
            );
        }
        expectedResponseGetAllTrafficAnUser = TrafficInfoResponse.builder()
                .traffics(traffics)
                .userID(userid)
                .count(traffics.size())
                .build();
        expectedResponseGetAllTrafficAnUserGroupByHour = new HashMap<>();
        Map<String, List<Traffic>> trafficInfoGroupByDate =
                traffics.stream().collect(Collectors.groupingBy(traffic -> {
                    String strDateTime = traffic.getTimeStamp();
                    return timeUtil.getHour(strDateTime);
                }));
        expectedResponseGetAllTrafficAnUserGroupByHour = trafficInfoGroupByDate;
    }

    @After
    public void destroy(){
        trafficsInfo.removeAll(trafficsInfo);
        expectedResponseGetAllTrafficAnUser = null;
    }

    @Test
    public void getTrafficWithFullParamRequestTest(){
        when(trafficInfoRepository.findTrafficInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(trafficsInfo);
        TrafficInfoResponse actualResponse = trafficService.getAllTrafficAnUser(userid, host, date, toDate);
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUser));
    }

    @Test
    public void getTrafficWithoutDateParamTest(){
        when(trafficInfoRepository.findTrafficInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(trafficsInfo);
        TrafficInfoResponse actualResponse = trafficService.getAllTrafficAnUser(userid, host, "", "");
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUser));
    }

    @Test
    public void getTrafficWithoutHostParamTest(){
        when(trafficInfoRepository.findTrafficInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(trafficsInfo);
        TrafficInfoResponse actualResponse = trafficService.getAllTrafficAnUser(userid, "", date, toDate);
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUser));
    }

    @Test
    public void getTrafficWithoutDateAndHostParamTest(){
        when(trafficInfoRepository.findTrafficInfo(userid, "", "", ""))
                .thenReturn(trafficsInfo);
        TrafficInfoResponse actualResponse = trafficService.getAllTrafficAnUser(userid, "", "", "");
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUser));
    }

    @Test
    public void getTrafficWithoutParamTest(){
        when(trafficInfoRepository.findTrafficInfo("", "", "", ""))
                .thenReturn(new ArrayList<>());
        TrafficInfoResponse actualResponse = trafficService.getAllTrafficAnUser("", "", "", "");
        assertThat(actualResponse, is(TrafficInfoResponse.builder().traffics(new ArrayList<>()).count(0).userID("").build()));
    }

    @Test
    public void getAllTrafficAnUserGroupByHourFullParamTest() throws InvalidInputException {
        when(trafficInfoRepository.findTrafficInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(trafficsInfo);
        Map<String, List<Traffic>> actualResponse = new HashMap<>();
        Assertions.assertThatCode(() -> trafficService.getAllTrafficAnUserGroupByHour(userid, host, date, toDate)).doesNotThrowAnyException();
        actualResponse = trafficService.getAllTrafficAnUserGroupByHour(userid, host, date, toDate);
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUserGroupByHour));
    }

    @Test
    public void getAllTrafficAnUserGroupByHourWithoutDateParam() throws InvalidInputException{
        when(trafficInfoRepository.findTrafficInfo(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(trafficsInfo);
        Map<String, List<Traffic>> actualResponse = new HashMap<>();
        Assertions.assertThatCode(() -> trafficService.getAllTrafficAnUserGroupByHour(userid, host, "", "")).doesNotThrowAnyException();
        actualResponse = trafficService.getAllTrafficAnUserGroupByHour(userid, host, "", "");
        assertThat(actualResponse, is(expectedResponseGetAllTrafficAnUserGroupByHour));
    }
}

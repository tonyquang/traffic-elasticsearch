package com.traffic.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traffic.report.dto.TrafficInfoResponse;
import com.traffic.report.exception.AuthenticationException;
import com.traffic.report.model.Traffic;
import com.traffic.report.model.TrafficInfo;
import com.traffic.report.services.TrafficExporterService;
import com.traffic.report.services.TrafficService;
import com.traffic.report.util.JwtUtil;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class TrafficRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;

    @MockBean
    private TrafficService trafficService;

    @MockBean
    private TrafficExporterService trafficExporterService;

    private String username = "admin";
    private String token;

    String host = "facebook";
    String userid = "quang.bui";
    String date = "2021-04-02";
    String toDate = "2021-04-05";

    TrafficInfoResponse expectedTrafficInfoResponse;

    @Before
    public void setup(){
        token = jwtUtil.generateToken(username);
        token = "Bearer "+token;
        assertNotNull(token);
        List<TrafficInfo> trafficsInfo = new ArrayList<>();
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
        expectedTrafficInfoResponse = TrafficInfoResponse.builder()
                .traffics(traffics)
                .userID(userid)
                .count(traffics.size())
                .build();
    }

    @Test
    public void getTrafficFullParamRequestTest() throws Exception {
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s?fromDate=%s&toDate=%s&host=%s", userid, date, toDate,host);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        ObjectMapper objectMapper = new ObjectMapper();
        String content =  mvcResult.getResponse().getContentAsString();
        TrafficInfoResponse actualResponse = objectMapper.readValue(content, TrafficInfoResponse.class);
        assertThat(actualResponse, is(expectedTrafficInfoResponse));
    }

    @Test
    public void getTrafficInvalidToDateLessThanFromDateParamTest() throws Exception {
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s?fromDate=%s&toDate=%s&host=%s", userid, toDate, date, host);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content =  mvcResult.getResponse().getContentAsString();
        assertThat(content, CoreMatchers.containsString("From date must be before To date"));
    }

    @Test
    public void getTrafficEmptyFromDateParamTest() throws Exception {
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s?fromDate=%s&toDate=%s&host=%s", userid, "", toDate, host);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(400, status);
        String content =  mvcResult.getResponse().getContentAsString();
        assertThat(content, CoreMatchers.containsString("You must input From date"));
    }

    @Test
    public void getTrafficWithoutHostParamTest() throws Exception{
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s?fromDate=%s&toDate=%s", userid, date, toDate);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        ObjectMapper objectMapper = new ObjectMapper();
        String content =  mvcResult.getResponse().getContentAsString();
        TrafficInfoResponse actualResponse = objectMapper.readValue(content, TrafficInfoResponse.class);
        assertThat(actualResponse, is(expectedTrafficInfoResponse));
    }

    @Test
    public void getTrafficWithoutDateParamTest() throws Exception{
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s?host=%s", userid, host);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        ObjectMapper objectMapper = new ObjectMapper();
        String content =  mvcResult.getResponse().getContentAsString();
        TrafficInfoResponse actualResponse = objectMapper.readValue(content, TrafficInfoResponse.class);
        assertThat(actualResponse, is(expectedTrafficInfoResponse));
    }

    @Test
    public void getTrafficWithoutDateAndHostParamTest() throws Exception{
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s", userid);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        ObjectMapper objectMapper = new ObjectMapper();
        String content =  mvcResult.getResponse().getContentAsString();
        TrafficInfoResponse actualResponse = objectMapper.readValue(content, TrafficInfoResponse.class);
        assertThat(actualResponse, is(expectedTrafficInfoResponse));
    }

    @Test
    public void getTrafficWithoutUserIDParamTest() throws Exception{
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/");
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url).header("Authorization", token)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getTrafficWithoutAuthorizationTest() throws Exception{
        when(this.trafficService.getAllTrafficAnUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(expectedTrafficInfoResponse);

        String url = String.format("/api/v1/traffic/%s", userid);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(403, status);
    }

    @Test
    public void getTokenSuccessTest() throws Exception {
        String url = "/api/v1/authenticate";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
            .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\":\"admin\",\"password\":\"admin\"}")
                .characterEncoding("utf-8")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content =  mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    public void getTokenUnAuthorizedTest() throws Exception {
        String url = "/api/v1/authenticate";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"user_name\":\"admin\",\"password\":\"wrongpass\"}")
                .characterEncoding("utf-8")
        ).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(401, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertThat(content, CoreMatchers.containsString("Invalid user/password"));
    }
}

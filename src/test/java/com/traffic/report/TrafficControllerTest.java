package com.traffic.report;

import com.traffic.report.exception.InvalidInputException;
import com.traffic.report.model.Traffic;
import com.traffic.report.services.TrafficExporterService;
import com.traffic.report.services.TrafficService;
import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = MonitorApplication.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@EnableWebMvc
public class TrafficControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TrafficExporterService trafficExporterService;

    @MockBean TrafficService trafficService;

    String host = "facebook";
    String userid = "quang.bui";
    String date = "2021-04-02";
    String toDate = "2021-04-05";


    @Test
    public void exportExcelFileFullParamSuccessTest() throws Exception {
        String url = String.format("/traffic/export-excel/%s?host=%s&fromDate=%s&toDate=%s", userid, host, date, toDate);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertNotNull(content);
    }

    @Test
    public void exportExcelFileWithoutHostParamSuccessTest() throws Exception {
        String url = String.format("/traffic/export-excel/%s?fromDate=%s&toDate=%s", userid, date, toDate);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertNotNull(content);
    }

    @Test
    public void exportExcelFileWithoutDateParamSuccessTest() throws Exception {
        String url = String.format("/traffic/export-excel/%s?host=%s", userid, host);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertNotNull(content);
    }

    @Test
    public void exportExcelFileWithoutHostAndDateParamSuccessTest() throws Exception {
        String url = String.format("/traffic/export-excel/%s", userid);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        assertEquals(200, status);
        assertNotNull(content);
    }

    @Test
    public void exportExcelFileWithoutUserIDTest() throws Exception {
        String url = "/traffic/export-excel/";
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    public void getLineChartFullParamTest() throws Exception {
        String url = String.format("/traffic/line-chart/%s?host=%s&fromDate=%s&toDate=%s", userid, host, date, toDate);
        Map<String, List<Traffic>> expected = new HashMap<>();
        when(trafficService
                .getAllTrafficAnUserGroupByHour(Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString())).thenReturn(expected);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }

    @Test
    public void getLineChartWithoutHostParamTest() throws Exception {
        String url = String.format("/traffic/line-chart/%s?fromDate=%s&toDate=%s", userid, date, toDate);
        Map<String, List<Traffic>> expected = new HashMap<>();
        when(trafficService
                .getAllTrafficAnUserGroupByHour(Mockito.anyString(), Mockito.anyString(),
                        Mockito.anyString(), Mockito.anyString())).thenReturn(expected);
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(url)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
        assertNotNull(content);
    }
}

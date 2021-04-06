package com.traffic.report.services;

import javax.servlet.http.HttpServletResponse;

public interface TrafficExporterService {
    void exportCSV(HttpServletResponse response, String userid, String host, String fromDate, String toDate);
}

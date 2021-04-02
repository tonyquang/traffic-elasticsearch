package com.traffic.report.services.imp;

import com.traffic.report.model.TrafficInfo;
import com.traffic.report.repository.TrafficInfoRepository;
import com.traffic.report.services.TrafficExporterService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class TrafficExporterServiceImp implements TrafficExporterService {

    @Autowired
    TrafficInfoRepository trafficInfoRepository;

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<TrafficInfo> trafficsInfo;

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Traffic");

        Row title = sheet.createRow(0);

        CellStyle styleTitle = workbook.createCellStyle();
        XSSFFont fontTitle = workbook.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeight(20);
        styleTitle.setFont(fontTitle);

        createCell(title, 0, String.format("Traffic Report of %s", trafficsInfo.get(0).getUserid()), styleTitle);

        Row row = sheet.createRow(1);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "URL", style);
        createCell(row, 1, "Time Stamp", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 2;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(13);
        style.setFont(font);

        for (TrafficInfo trafficInfo : trafficsInfo) {
            int columnCount = 0;
            Row row = sheet.createRow(rowCount++);
            createCell(row, columnCount++, trafficInfo.getUrl(), style);
            createCell(row, columnCount++, trafficInfo.getLocaldate(), style);
        }
    }


    @Override
    public void exportCSV(HttpServletResponse response, String userid, String host, String time) {
        trafficsInfo = trafficInfoRepository.findTrafficInfo(userid,host,time);
        buildExcelFile(response);
    }

    private void buildExcelFile(HttpServletResponse response){
        workbook = new XSSFWorkbook();
        writeHeaderLine();
        writeDataLines();
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

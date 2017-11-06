package excel;

import dto.TickerInfoDTO;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

public class ApachePOIExcelWrite {
    public static void writeExcel(Map<String, List<TickerInfoDTO>> planilhasPortfolio, String excelFilePath) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();

        planilhasPortfolio.forEach((sheetName,sheetListInfo)->{
            System.out.println("Started writing sheet: " + sheetName);
            XSSFSheet sheet = workbook.createSheet(sheetName);

            int rowCount = 0;
            writeFileHeader(sheet.createRow(rowCount));
            for (TickerInfoDTO tickerInfoDTO : sheetListInfo) {
                Row row = sheet.createRow(rowCount++);
                writeTickerInfoRow(tickerInfoDTO, row);
            }
        });

        try {
            File excelFile = new File(excelFilePath);
            Files.deleteIfExists(excelFile.toPath());
            FileOutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeTickerInfoRow(TickerInfoDTO tickerInfo, Row row) {
        Cell cell = row.createCell(1);
        cell.setCellValue(tickerInfo.getTicker());

        cell = row.createCell(2);
        cell.setCellValue(tickerInfo.getAno());

        cell = row.createCell(3);
        if(tickerInfo.getSize() == null){
            cell.setCellValue("null");
        } else {
            cell.setCellValue(tickerInfo.getSize());
        }

        cell = row.createCell(4);
        if(tickerInfo.getBmme() == null){
            cell.setCellValue("null");
        } else {
            cell.setCellValue(tickerInfo.getBmme());
        }

        cell = row.createCell(5);
        if(tickerInfo.getOp() == null){
            cell.setCellValue("null");
        } else {
            cell.setCellValue(tickerInfo.getOp());
        }

        cell = row.createCell(6);
        if(tickerInfo.getInv() == null){
            cell.setCellValue("null");
        } else {
            cell.setCellValue(tickerInfo.getInv());
        }
    }

    private static void writeFileHeader(Row row) {
        Cell cell = row.createCell(0);
        cell.setCellValue("Ticker");

        cell = row.createCell(1);
        cell.setCellValue("Ano");

        cell = row.createCell(2);
        cell.setCellValue("SIZE");

        cell = row.createCell(3);
        cell.setCellValue("BMME");

        cell = row.createCell(4);
        cell.setCellValue("OP");

        cell = row.createCell(5);
        cell.setCellValue("INV");
    }

}
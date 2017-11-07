package excel;

import dto.TickerInfoDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReader {


//    private static

    public static List<TickerInfoDTO> getTickers(String diretorio){
        List<TickerInfoDTO> amostra = new ArrayList<>();

        try {
            System.out.println("Started reading file: " + diretorio);
            FileInputStream excelFile = new FileInputStream(new File(diretorio));
            Workbook workbook = new XSSFWorkbook(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);

            for (Row currentRow : datatypeSheet) {
                if(currentRow.getRowNum() == 0){
                    continue;
                }
                Iterator<Cell> cellIterator = currentRow.iterator();
                TickerInfoDTO tickerInfoDTO = new TickerInfoDTO();

                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();

                    switch (columnIndex) {
                        case 0:
                            tickerInfoDTO.setTicker(getCellValue(nextCell) == null ? null : (String) getCellValue(nextCell));
                            break;
                        case 1:
                            tickerInfoDTO.setAno(getCellValue(nextCell) == null ? null : (String) getCellValue(nextCell));
                            break;
                        case 2:
                            tickerInfoDTO.setSize(getCellValue(nextCell) == null ? null : (Double) getCellValue(nextCell));
                            break;
                        case 3:
                            tickerInfoDTO.setBmme(getCellValue(nextCell) == null ? null : (Double) getCellValue(nextCell));
                            break;
                        case 4:
                            tickerInfoDTO.setOp(getCellValue(nextCell) == null ? null : (Double) getCellValue(nextCell));
                            break;
                        case 5:
                            tickerInfoDTO.setInv(getCellValue(nextCell) == null ? null : (Double) getCellValue(nextCell));
                            break;
                    }
                }
                amostra.add(tickerInfoDTO);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return amostra;
    }

    private static Object getCellValue(Cell currentCell) {
        if (currentCell.getCellTypeEnum() == CellType.STRING && currentCell.getStringCellValue().equalsIgnoreCase("null")) {
            return null;
        } else if (currentCell.getCellTypeEnum() == CellType.STRING) {
            return currentCell.getStringCellValue();
        } else if (currentCell.getCellTypeEnum() == CellType.NUMERIC) {
            return currentCell.getNumericCellValue();
        }
        return null;
    }
}
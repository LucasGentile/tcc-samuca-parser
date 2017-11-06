import dto.TickerInfoDTO;
import excel.ApachePOIExcelRead;
import excel.ApachePOIExcelWrite;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickers {

    private static final String BASE_DIRECTORY = "C:\\Samuca\\Dados Anuais\\";
    private static final String YEAR = "2016";
    private static final String FILE_NAME = "\\" + YEAR + ".xlsx";
    private static final String PORTFOLIO_NAME = "\\" + YEAR + "_Portfolio50304030.xlsx";

    private static final String READ_DIRECTORY = BASE_DIRECTORY + YEAR + FILE_NAME;
    private static final String WRITE_DIRECTORY = BASE_DIRECTORY + YEAR + PORTFOLIO_NAME;

    public static void main(String[] args) {
        //Reading sample file
        List<TickerInfoDTO> amostra = ApachePOIExcelRead.getAmostra(READ_DIRECTORY);

        //Applying sorting and filtering
        //Creating sheets
        Map<String, List<TickerInfoDTO>> portfolio = new HashMap<>();
        portfolio.put("A",amostra);
        portfolio.put("B",amostra);
        portfolio.put("C",amostra);

        try {
            ApachePOIExcelWrite.writeExcel(portfolio, WRITE_DIRECTORY);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

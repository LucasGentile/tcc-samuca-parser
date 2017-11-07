import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static enums.PortfolioGroupNamesEnum.B;
import static enums.PortfolioGroupNamesEnum.S;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickersPortfolioB {
    private static final String BASE_DIRECTORY = "C:\\Samuca\\Dados Anuais\\";

    private static final String[] YEARS_LIST = {"2016",
//                                                "2015",
//                                                "2014",
//                                                "2013",
//                                                "2012",
//                                                "2011",
//                                                "2010",
//                                                "2009",
//                                                "2008",
//                                                "2007",
//                                                "2006",
//                                                "2005",
//                                                "2004",
//                                                "2003",
//                                                "2002"
    };


    public static void main(String[] args) {

        for (String year : YEARS_LIST) {
            System.out.println("\n################ Started processing Tickers from " + year);

            String fileName = "\\" + year + ".xlsx";
            String portfolioName = "\\" + year + "_Portfolio5050.xlsx";

            String readDirectory = BASE_DIRECTORY + year + fileName;
            String writeDirectory = BASE_DIRECTORY + year + portfolioName;

            //Reading sample file related to the current year
            List<TickerInfoDTO> allTickers = ExcelReader.getTickers(readDirectory);

            //Create portfolio map
            SortedMap<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap = new TreeMap<>();

            System.out.println("## Sorting all tickers by size");
            allTickers.sort(Comparator.comparingDouble(TickerInfoDTO::getSize));

            generateTickersSpreedsheets(allTickers, portfolioMap);

            try {
                ExcelWriter.writeExcel(portfolioMap, writeDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generateTickersSpreedsheets(List<TickerInfoDTO> allTickers, Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap) {
        int tickersTotalSize = allTickers.size();
        System.out.println("Total Size: " + tickersTotalSize);


        System.out.println("#### Creating S");

        int sizeS = Math.round(tickersTotalSize / 2);

        List<TickerInfoDTO> listS = new ArrayList<>(allTickers.stream().limit(sizeS).collect(Collectors.toList()));
        portfolioMap.put(S, listS);

        System.out.println("S Size: " + listS.size());

//////
        System.out.println("#### Creating B");


        List<TickerInfoDTO> listB = new ArrayList<>(allTickers.stream().skip(sizeS).collect(Collectors.toList()));
        portfolioMap.put(B, listB);

        System.out.println("B Size: " + listB.size());


    }


}

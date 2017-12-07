import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;

import static enums.PortfolioGroupNamesEnum.*;
import static enums.SortingTypeEnum.*;
import static utils.TickersListUtils.*;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickersPortfolioB {
    private static final String BASE_DIRECTORY = "C:\\Samuca\\Dados Anuais\\";

    private static final String[] YEARS_LIST = {"2016",
                                                "2015",
                                                "2014",
                                                "2013",
                                                "2012",
                                                "2011",
                                                "2010",
                                                "2009",
                                                "2008",
                                                "2007",
                                                "2006",
                                                "2005",
                                                "2004",
                                                "2003",
                                                "2002"
    };


    public static void main(String[] args) {

        for (String year : YEARS_LIST) {
            String fileName = "\\" + year + ".xlsx";
            String portfolioName = "\\" + year + "_Portfolio5050.xlsx";

            System.out.println("\n################ Started processing Tickers for " + portfolioName);

            String readDirectory = BASE_DIRECTORY + year + fileName;
            String writeDirectory = BASE_DIRECTORY + year + portfolioName;

            //Reading sample file related to the current year
            List<TickerInfoDTO> allTickers = ExcelReader.getTickers(readDirectory);

            //Create portfolio map
            SortedMap<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap = new TreeMap<>();



            generateTickersSpreadsheets(allTickers, portfolioMap);

            try {
                ExcelWriter.writeExcel(portfolioMap, writeDirectory);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void generateTickersSpreadsheets(List<TickerInfoDTO> allTickers, Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap) {
        int tickersTotalSize = allTickers.size();
        System.out.println("Total Size: " + tickersTotalSize);

        List<TickerInfoDTO> listAllTickersWithoutAnyNullValue = sortTickersBySizeWithoutAnyNullValue(allTickers);

        int sizeSWithoutNull = Math.round(listAllTickersWithoutAnyNullValue.size() / 2);

        List<TickerInfoDTO> listS = getDividedList(listAllTickersWithoutAnyNullValue, sizeSWithoutNull, S, true);
        PortfolioGroupNamesEnum[] S_SUBGROUPS = {S_L,S_H,S_W,S_R,S_A,S_C,S_L_R_A,S_L_R_C,S_L_W_A,S_L_W_C,S_H_R_A,S_H_R_C,S_H_W_A,S_H_W_C};
        getSubgroupsLists(portfolioMap,listAllTickersWithoutAnyNullValue, listS, S_SUBGROUPS);

        List<TickerInfoDTO> listB = getDividedList(listAllTickersWithoutAnyNullValue, sizeSWithoutNull, B, false);
        PortfolioGroupNamesEnum[] B_SUBGROUPS = {B_L,B_H,B_W,B_R,B_A,B_C,B_L_R_A,B_L_R_C,B_L_W_A,B_L_W_C,B_H_R_A,B_H_R_C,B_H_W_A,B_H_W_C};
        getSubgroupsLists(portfolioMap,listAllTickersWithoutAnyNullValue, listB, B_SUBGROUPS);
    }

    private static void getSubgroupsLists(Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap,List<TickerInfoDTO> allTickersList, List<TickerInfoDTO> halfTickersList, PortfolioGroupNamesEnum[] SUBGROUPS) {
        int fiftyPercentAllTickersList = Math.round(allTickersList.size() / 2);

        List<TickerInfoDTO> listSortedByBMME = sortTickersByType(allTickersList, ALL, BMME);
        List<TickerInfoDTO> listL = getDividedList(listSortedByBMME, fiftyPercentAllTickersList, SUBGROUPS[0], true);
        List<TickerInfoDTO> listH = getDividedList(listSortedByBMME, fiftyPercentAllTickersList, SUBGROUPS[1], false);

        List<TickerInfoDTO> listSortedByOP = sortTickersByType(allTickersList, ALL, OP);
        List<TickerInfoDTO> listW = getDividedList(listSortedByOP, fiftyPercentAllTickersList, SUBGROUPS[2], true);
        List<TickerInfoDTO> listR = getDividedList(listSortedByOP, fiftyPercentAllTickersList, SUBGROUPS[3], false);

        List<TickerInfoDTO> listSortedByINV = sortTickersByType(allTickersList, ALL, INV);
        List<TickerInfoDTO> listA = getDividedList(listSortedByINV, fiftyPercentAllTickersList, SUBGROUPS[4], true);
        List<TickerInfoDTO> listC = getDividedList(listSortedByINV, fiftyPercentAllTickersList, SUBGROUPS[5], false);

        List<TickerInfoDTO> listLRA = new ArrayList<>();
        List<TickerInfoDTO> listLRC = new ArrayList<>();
        List<TickerInfoDTO> listLWA = new ArrayList<>();
        List<TickerInfoDTO> listLWC = new ArrayList<>();
        List<TickerInfoDTO> listHRA = new ArrayList<>();
        List<TickerInfoDTO> listHRC = new ArrayList<>();
        List<TickerInfoDTO> listHWA = new ArrayList<>();
        List<TickerInfoDTO> listHWC = new ArrayList<>();

        for (TickerInfoDTO tickers : halfTickersList) {
            if(listL.contains(tickers) && listR.contains(tickers) && listA.contains(tickers)){
                listLRA.add(tickers);
            } else if(listL.contains(tickers) && listR.contains(tickers) && listC.contains(tickers)){
                listLRC.add(tickers);
            } else if(listL.contains(tickers) && listW.contains(tickers) && listA.contains(tickers)){
                listLWA.add(tickers);
            } else if(listL.contains(tickers) && listW.contains(tickers) && listC.contains(tickers)){
                listLWC.add(tickers);
            } else if(listH.contains(tickers) && listR.contains(tickers) && listC.contains(tickers)){
                listHRC.add(tickers);
            } else if(listH.contains(tickers) && listR.contains(tickers) && listA.contains(tickers)){
                listHRA.add(tickers);
            } else if(listH.contains(tickers) && listW.contains(tickers) && listA.contains(tickers)){
                listHWA.add(tickers);
            } else if(listH.contains(tickers) && listW.contains(tickers) && listC.contains(tickers)){
                listHWC.add(tickers);
            }
        }

        createSubGroup(listLRA, portfolioMap, SUBGROUPS[6]);
        createSubGroup(listLRC, portfolioMap, SUBGROUPS[7]);
        createSubGroup(listLWA, portfolioMap, SUBGROUPS[8]);
        createSubGroup(listLWC, portfolioMap, SUBGROUPS[9]);
        createSubGroup(listHRA, portfolioMap, SUBGROUPS[10]);
        createSubGroup(listHRC, portfolioMap, SUBGROUPS[11]);
        createSubGroup(listHWA, portfolioMap, SUBGROUPS[12]);
        createSubGroup(listHWC, portfolioMap, SUBGROUPS[13]);
    }
}

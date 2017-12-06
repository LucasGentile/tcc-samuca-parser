import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
        List<TickerInfoDTO> listSSortedByBM = sortTickersByType(listS, S, BMME);
        PortfolioGroupNamesEnum[] S_SUBGROUPS = {S_L,S_L_W,S_L_W_A,S_L_W_C,S_L_R,S_L_R_A,S_L_R_C,S_H,S_H_W,S_H_W_A,S_H_W_C,S_H_R,S_H_R_A,S_H_R_C};
        getSubgroupsLists(portfolioMap, listSSortedByBM, S_SUBGROUPS);

        List<TickerInfoDTO> listB = getDividedList(listAllTickersWithoutAnyNullValue, sizeSWithoutNull, B, false);
        List<TickerInfoDTO> listBSortedByBM = sortTickersByType(listB, B, BMME);
        PortfolioGroupNamesEnum[] B_SUBGROUPS = {B_L,B_L_W,B_L_W_A,B_L_W_C,B_L_R,B_L_R_A,B_L_R_C,B_H,B_H_W,B_H_W_A,B_H_W_C,B_H_R,B_H_R_A,B_H_R_C};
        getSubgroupsLists(portfolioMap, listBSortedByBM, B_SUBGROUPS);
    }

    private static void getSubgroupsLists(Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, List<TickerInfoDTO> tickersList, PortfolioGroupNamesEnum[] SUBGROUPS) {
        int sizeSortedByBM = Math.round(tickersList.size() / 2);

        List<TickerInfoDTO> listL = getDividedList(tickersList, sizeSortedByBM, SUBGROUPS[0], true);

        List<TickerInfoDTO> listLSortedByOP = sortTickersByType(listL, SUBGROUPS[0], OP);
        int sizeLSortedByOP = Math.round(listLSortedByOP.size() / 2);

        List<TickerInfoDTO> listLW = getDividedList(listLSortedByOP, sizeLSortedByOP, SUBGROUPS[1], true);

        List<TickerInfoDTO> listLWSortedByINV = sortTickersByType(listLW, SUBGROUPS[1], INV);
        int sizeLWSortedByINV = Math.round(listLWSortedByINV.size() / 2);

        List<TickerInfoDTO> listLWA = getDividedList(listLWSortedByINV, sizeLWSortedByINV, SUBGROUPS[2], true);
        createSubGroup(listLWA, portfolioMap, SUBGROUPS[2]);
        List<TickerInfoDTO> listLWC = getDividedList(listLWSortedByINV, sizeLWSortedByINV, SUBGROUPS[3], false);
        createSubGroup(listLWC, portfolioMap, SUBGROUPS[3]);

        List<TickerInfoDTO> listLR = getDividedList(listLSortedByOP, sizeLSortedByOP, SUBGROUPS[4], false);

        List<TickerInfoDTO> listLRSortedByINV = sortTickersByType(listLR, SUBGROUPS[4], INV);
        int sizeLRSortedByINV = Math.round(listLRSortedByINV.size() / 2);

        List<TickerInfoDTO> listLRA = getDividedList(listLRSortedByINV, sizeLRSortedByINV, SUBGROUPS[5], true);
        createSubGroup(listLRA, portfolioMap, SUBGROUPS[5]);
        List<TickerInfoDTO> listLRC = getDividedList(listLRSortedByINV, sizeLRSortedByINV, SUBGROUPS[6], false);
        createSubGroup(listLRC, portfolioMap, SUBGROUPS[6]);

        List<TickerInfoDTO> listH = getDividedList(tickersList, sizeSortedByBM, SUBGROUPS[7], false);

        List<TickerInfoDTO> listHSortedByOP = sortTickersByType(listH, SUBGROUPS[7], OP);
        int sizeHSortedByOP = Math.round(listHSortedByOP.size() / 2);

        List<TickerInfoDTO> listHW = getDividedList(listHSortedByOP, sizeHSortedByOP, SUBGROUPS[8], true);

        List<TickerInfoDTO> listHWSortedByINV = sortTickersByType(listHW, SUBGROUPS[8], INV);
        int sizeHWSortedByINV = Math.round(listHWSortedByINV.size() / 2);

        List<TickerInfoDTO> listHWA = getDividedList(listHWSortedByINV, sizeHWSortedByINV, SUBGROUPS[9], true);
        createSubGroup(listHWA, portfolioMap, SUBGROUPS[9]);
        List<TickerInfoDTO> listHWC = getDividedList(listHWSortedByINV, sizeHWSortedByINV, SUBGROUPS[10], false);
        createSubGroup(listHWC, portfolioMap, SUBGROUPS[10]);

        List<TickerInfoDTO> listHR = getDividedList(listHSortedByOP, sizeHSortedByOP,  SUBGROUPS[11], false);

        List<TickerInfoDTO> listHRSortedByINV = sortTickersByType(listHR,  SUBGROUPS[11], INV);
        int sizeHRSortedByINV = Math.round(listHRSortedByINV.size() / 2);

        List<TickerInfoDTO> listHRA = getDividedList(listHRSortedByINV, sizeHRSortedByINV, SUBGROUPS[12], true);
        createSubGroup(listHRA, portfolioMap, SUBGROUPS[12]);
        List<TickerInfoDTO> listHRC = getDividedList(listHRSortedByINV, sizeHRSortedByINV, SUBGROUPS[13], false);
        createSubGroup(listHRC, portfolioMap, SUBGROUPS[13]);
    }
}

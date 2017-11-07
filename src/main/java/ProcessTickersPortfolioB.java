import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;

import static enums.PortfolioGroupNamesEnum.*;
import static enums.SortingTypeEnum.*;
import static utils.TickersListUtils.getDividedList;
import static utils.TickersListUtils.sortTickersByTypeWithoutNull;

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
            String fileName = "\\" + year + ".xlsx";
            String portfolioName = "\\" + year + "_Portfolio5050.xlsx";

            System.out.println("\n################ Started processing Tickers for " + portfolioName);

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

        int sizeS = Math.round(tickersTotalSize / 2);

        List<TickerInfoDTO> listS = getDividedList(allTickers, portfolioMap, sizeS, S, true);
        List<TickerInfoDTO> listSSortedByBM = sortTickersByTypeWithoutNull(listS, S, BMME);
        PortfolioGroupNamesEnum[] S_SUBGROUPS = {S_L5,S_L5_W,S_L5_W_A,S_L5_W_C,S_L5_R,S_L5_R_A,S_L5_R_C,S_H5,S_H5_R,S_H5_W,S_H5_W_A,S_H5_W_C,S_H5_R_A,S_H5_R_C};
        getSubgroupsLists(portfolioMap, listSSortedByBM, S_SUBGROUPS);

        List<TickerInfoDTO> listB = getDividedList(allTickers, portfolioMap, sizeS, B, false);
        List<TickerInfoDTO> listBSortedByBM = sortTickersByTypeWithoutNull(listB, B, BMME);
        PortfolioGroupNamesEnum[] B_SUBGROUPS = {B_L5,B_L5_W,B_L5_W_A,B_L5_W_C,B_L5_R,B_L5_R_A,B_L5_R_C,B_H5,B_H5_R,B_H5_W,B_H5_W_A,B_H5_W_C,B_H5_R_A,B_H5_R_C};
        getSubgroupsLists(portfolioMap, listBSortedByBM, B_SUBGROUPS);
    }

    private static void getSubgroupsLists(Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, List<TickerInfoDTO> tickersList, PortfolioGroupNamesEnum[] SUBGROUPS) {
        int sizeSortedByBM = Math.round(tickersList.size() / 2);

        List<TickerInfoDTO> listL5 = getDividedList(tickersList, portfolioMap, sizeSortedByBM, SUBGROUPS[0], true);

        List<TickerInfoDTO> listL5SortedByOP = sortTickersByTypeWithoutNull(listL5, SUBGROUPS[0], OP);
        int sizeL5SortedByOP = Math.round(listL5SortedByOP.size() / 2);

        List<TickerInfoDTO> listL5W = getDividedList(listL5SortedByOP, portfolioMap, sizeL5SortedByOP, SUBGROUPS[1], true);

        List<TickerInfoDTO> listL5WSortedByINV = sortTickersByTypeWithoutNull(listL5W, SUBGROUPS[1], INV);
        int sizeL5WSortedByINV = Math.round(listL5WSortedByINV.size() / 2);

        List<TickerInfoDTO> listL5WA = getDividedList(listL5WSortedByINV, portfolioMap, sizeL5WSortedByINV, SUBGROUPS[2], true);
        List<TickerInfoDTO> listL5WC = getDividedList(listL5WSortedByINV, portfolioMap, sizeL5WSortedByINV, SUBGROUPS[3], false);

        List<TickerInfoDTO> listL5R = getDividedList(listL5SortedByOP, portfolioMap, sizeL5SortedByOP, SUBGROUPS[4], false);

        List<TickerInfoDTO> listL5RSortedByINV = sortTickersByTypeWithoutNull(listL5R, SUBGROUPS[4], INV);
        int sizeL5RSortedByINV = Math.round(listL5RSortedByINV.size() / 2);

        List<TickerInfoDTO> listL5RA = getDividedList(listL5RSortedByINV, portfolioMap, sizeL5RSortedByINV, SUBGROUPS[5], true);
        List<TickerInfoDTO> listL5RC = getDividedList(listL5RSortedByINV, portfolioMap, sizeL5RSortedByINV, SUBGROUPS[6], false);

        List<TickerInfoDTO> listH5 = getDividedList(tickersList, portfolioMap, sizeSortedByBM, SUBGROUPS[7], false);

        List<TickerInfoDTO> listH5SortedByOP = sortTickersByTypeWithoutNull(listH5, SUBGROUPS[7], OP);
        int sizeH5SortedByOP = Math.round(listH5SortedByOP.size() / 2);

        List<TickerInfoDTO> listH5W = getDividedList(listH5SortedByOP, portfolioMap, sizeH5SortedByOP, SUBGROUPS[8], true);

        List<TickerInfoDTO> listH5WSortedByINV = sortTickersByTypeWithoutNull(listH5W, SUBGROUPS[8], INV);
        int sizeH5WSortedByINV = Math.round(listH5WSortedByINV.size() / 2);

        List<TickerInfoDTO> listH5WA = getDividedList(listH5WSortedByINV, portfolioMap, sizeH5WSortedByINV, SUBGROUPS[9], true);
        List<TickerInfoDTO> listH5WC = getDividedList(listH5WSortedByINV, portfolioMap, sizeH5WSortedByINV, SUBGROUPS[10], false);

        List<TickerInfoDTO> listH5R = getDividedList(listH5SortedByOP, portfolioMap, sizeH5SortedByOP, S_H5_R, false);

        List<TickerInfoDTO> listH5RSortedByINV = sortTickersByTypeWithoutNull(listH5R, S_H5_R, INV);
        int sizeH5RSortedByINV = Math.round(listH5RSortedByINV.size() / 2);

        List<TickerInfoDTO> listH5RA = getDividedList(listH5RSortedByINV, portfolioMap, sizeH5RSortedByINV, SUBGROUPS[11], true);
        List<TickerInfoDTO> listH5RC = getDividedList(listH5RSortedByINV, portfolioMap, sizeH5RSortedByINV, SUBGROUPS[12], false);
    }
}

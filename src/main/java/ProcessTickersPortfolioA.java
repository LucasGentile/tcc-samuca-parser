import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static enums.PortfolioGroupNamesEnum.*;
import static enums.SortingTypeEnum.*;
import static utils.TickersListUtils.*;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickersPortfolioA {
    private static final String BASE_DIRECTORY = "C:\\Samuca\\Dados Anuais\\";

    private static final String[] YEARS_LIST = {
            "2016",
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
            String portfolioName = "\\" + year + "_Portfolio50304030.xlsx";

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

        // S
        List<TickerInfoDTO> listS = getDividedList(listAllTickersWithoutAnyNullValue, sizeSWithoutNull, S, true);
        List<TickerInfoDTO> listBM = sortTickersByType(listAllTickersWithoutAnyNullValue, ALL, BMME);
        List<TickerInfoDTO> listOP = sortTickersByType(listAllTickersWithoutAnyNullValue, ALL, OP);
        List<TickerInfoDTO> listINV = sortTickersByType(listAllTickersWithoutAnyNullValue, ALL, INV);

        PortfolioGroupNamesEnum[] S_BMME_SUBGROUPS = {S_L3,S_BMME4,S_H3};
        getSubgroupsLists(portfolioMap,listS, listBM, S_BMME_SUBGROUPS);

        PortfolioGroupNamesEnum[] S_OP_SUBGROUPS = {S_W3,S_OP4,S_R3};
        getSubgroupsLists(portfolioMap,listS, listOP, S_OP_SUBGROUPS);

        PortfolioGroupNamesEnum[] S_INV_SUBGROUPS = {S_A3,S_INV4,S_C3};
        getSubgroupsLists(portfolioMap,listS, listINV, S_INV_SUBGROUPS);

        // B
        List<TickerInfoDTO> listB = getDividedList(listAllTickersWithoutAnyNullValue, sizeSWithoutNull, B, false);
        List<TickerInfoDTO> listBBM = sortTickersByType(listAllTickersWithoutAnyNullValue, B, BMME);
        List<TickerInfoDTO> listBOP = sortTickersByType(listAllTickersWithoutAnyNullValue, B, OP);
        List<TickerInfoDTO> listBINV = sortTickersByType(listAllTickersWithoutAnyNullValue, B, INV);

        PortfolioGroupNamesEnum[] B_BMME_SUBGROUPS = {B_L3,B_BMME4,B_H3};
        getSubgroupsLists(portfolioMap,listB, listBBM, B_BMME_SUBGROUPS);

        PortfolioGroupNamesEnum[] B_OP_SUBGROUPS = {B_W3,B_OP4,B_R3};
        getSubgroupsLists(portfolioMap,listB, listBOP, B_OP_SUBGROUPS);

        PortfolioGroupNamesEnum[] B_INV_SUBGROUPS = {B_A3,B_INV4,B_C3};
        getSubgroupsLists(portfolioMap,listB, listBINV, B_INV_SUBGROUPS);

    }

    private static void getSubgroupsLists(Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, List<TickerInfoDTO> allList, List<TickerInfoDTO> groupList, PortfolioGroupNamesEnum[] subgroupNames) {

        int percentThirtyMinor = Math.round(30 * groupList.size() / 100);
        List<TickerInfoDTO> listThirtyMinor = new ArrayList<>(groupList.stream().limit(percentThirtyMinor).collect(Collectors.toList()));

        int percentFortyMedian = Math.round(40 * groupList.size() / 100);
        List<TickerInfoDTO> listFortyMedian = new ArrayList<>(groupList.stream().skip(percentThirtyMinor).limit(percentFortyMedian).collect(Collectors.toList()));

        int percentThirtyBigger = (percentThirtyMinor + percentFortyMedian);
        List<TickerInfoDTO> listThirtyBigger = new ArrayList<>(groupList.stream().skip(percentThirtyBigger).collect(Collectors.toList()));

        List<TickerInfoDTO> listAllAndThirtyMinor = new ArrayList<>();
        List<TickerInfoDTO> listAllAndFortyMedian = new ArrayList<>();
        List<TickerInfoDTO> listAllAndThirtyBigger = new ArrayList<>();

        for (TickerInfoDTO ticker : allList) {
            if(listThirtyMinor.contains(ticker)){
                listAllAndThirtyMinor.add(ticker);
            }else if(listFortyMedian.contains(ticker)){
                listAllAndFortyMedian.add(ticker);
            }else if(listThirtyBigger.contains(ticker)){
                listAllAndThirtyBigger.add(ticker);
            }
        }

        System.out.println("## Creating " + subgroupNames[0] + " (30% -)");
        portfolioMap.put(subgroupNames[0], listAllAndThirtyMinor);
        System.out.println(subgroupNames[0].name() + " size: " + listAllAndThirtyMinor.size());

        System.out.println("## Creating " + subgroupNames[1] + " (40% +-)");
        portfolioMap.put(subgroupNames[1], listAllAndFortyMedian);
        System.out.println(subgroupNames[1].name() + " size: " + listAllAndFortyMedian.size());

        System.out.println("## Creating " + subgroupNames[2] + " (30% +)");
        portfolioMap.put(subgroupNames[2], listAllAndThirtyBigger);
        System.out.println(subgroupNames[2].name() + " size: " + listAllAndThirtyBigger.size());
    }



}

import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static enums.PortfolioGroupNamesEnum.*;
import static enums.SortingTypeEnum.*;
import static utils.TickersListUtils.getDividedList;
import static utils.TickersListUtils.sortTickersByTypeWithoutNull;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickersPortfolioA {
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
            String portfolioName = "\\" + year + "_Portfolio50304030.xlsx";

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

        // S
        List<TickerInfoDTO> listS = getDividedList(allTickers, portfolioMap, sizeS, S, true);

        List<TickerInfoDTO> listSBM = sortTickersByTypeWithoutNull(listS, S, BMME);
        PortfolioGroupNamesEnum[] S_BMME_SUBGROUPS = {S_L3,S_NBM4,S_H3};
        getSubgroupsLists(portfolioMap, listSBM, S_BMME_SUBGROUPS);

        List<TickerInfoDTO> listSOP = sortTickersByTypeWithoutNull(listS, S, OP);
        PortfolioGroupNamesEnum[] S_OP_SUBGROUPS = {S_W3,S_NOP4,S_R3};
        getSubgroupsLists(portfolioMap, listSOP, S_OP_SUBGROUPS);

        List<TickerInfoDTO> listSINV = sortTickersByTypeWithoutNull(listS, S, INV);
        PortfolioGroupNamesEnum[] S_INV_SUBGROUPS = {S_C3,S_NINV4,S_A3};
        getSubgroupsLists(portfolioMap, listSINV, S_INV_SUBGROUPS);

        // B
        List<TickerInfoDTO> listB = getDividedList(allTickers, portfolioMap, sizeS, B, true);

        List<TickerInfoDTO> listBBM = sortTickersByTypeWithoutNull(listB, B, BMME);
        PortfolioGroupNamesEnum[] B_BMME_SUBGROUPS = {B_L3,B_NBM4,B_H3};
        getSubgroupsLists(portfolioMap, listBBM, B_BMME_SUBGROUPS);

        List<TickerInfoDTO> listBOP = sortTickersByTypeWithoutNull(listB, B, OP);
        PortfolioGroupNamesEnum[] B_OP_SUBGROUPS = {B_W3,B_NOP4,B_R3};
        getSubgroupsLists(portfolioMap, listBOP, B_OP_SUBGROUPS);

        List<TickerInfoDTO> listBINV = sortTickersByTypeWithoutNull(listB, B, INV);
        PortfolioGroupNamesEnum[] B_INV_SUBGROUPS = {B_C3,B_NINV4,B_A3};
        getSubgroupsLists(portfolioMap, listBINV, B_INV_SUBGROUPS);

    }

    private static void getSubgroupsLists(Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, List<TickerInfoDTO> groupList, PortfolioGroupNamesEnum[] subgroupNames) {

        System.out.println("## Creating " + subgroupNames[0] + " (30% -)");
        int percentThirtyMinor = Math.round(30 * groupList.size() / 100);
        List<TickerInfoDTO> listThirtyMinor = new ArrayList<>(groupList.stream().limit(percentThirtyMinor).collect(Collectors.toList()));
        portfolioMap.put(subgroupNames[0], listThirtyMinor);
        System.out.println(subgroupNames[0].name() + " size: " + listThirtyMinor.size());

        System.out.println("## Creating " + subgroupNames[1] + " (40% +-)");
        int percentFortyMedian = Math.round(40 * groupList.size() / 100);
        List<TickerInfoDTO> listFortyMedian = new ArrayList<>(groupList.stream().skip(percentThirtyMinor).limit(percentFortyMedian).collect(Collectors.toList()));
        portfolioMap.put(subgroupNames[1], listFortyMedian);
        System.out.println(subgroupNames[1].name() + " size: " + listFortyMedian.size());

        System.out.println("## Creating " + subgroupNames[2] + " (30% +)");
        int percentThirtyBigger = (percentThirtyMinor + percentFortyMedian);
        List<TickerInfoDTO> listThirtyBigger = new ArrayList<>(groupList.stream().skip(percentThirtyBigger).collect(Collectors.toList()));
        portfolioMap.put(subgroupNames[2], listThirtyBigger);
        System.out.println(subgroupNames[2].name() + " size: " + listThirtyBigger.size());
    }



}

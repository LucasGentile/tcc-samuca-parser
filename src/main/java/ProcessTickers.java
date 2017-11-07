import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import excel.ExcelReader;
import excel.ExcelWriter;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static enums.PortfolioGroupNamesEnum.*;

/**
 * Created by lucas on 05/11/2017.
 */
public class ProcessTickers {
    private static final String BASE_DIRECTORY = "C:\\Samuca\\Dados Anuais\\";

    private static final String[] YEARS_LIST = {"2016",
//                                                "2015",
//                                                "2014",
//                                                "2013",
                                                "2012",
//                                                "2011",
//                                                "2010",
//                                                "2009",
//                                                "2008",
                                                "2007",
//                                                "2006",
//                                                "2005",
//                                                "2004",
//                                                "2003",
                                                "2002"};



    public static void main(String[] args) {

        for (String year : YEARS_LIST) {
            System.out.println("\n################ Started processing Tickers from " + year);

            String fileName = "\\" + year + ".xlsx";
            String portfolioName = "\\" + year + "_Portfolio50304030.xlsx";

            String readDirectory = BASE_DIRECTORY + year + fileName;
            String writeDirectory = BASE_DIRECTORY + year + portfolioName;

            //Reading sample file related to the current year
            List<TickerInfoDTO> allTickers = ExcelReader.getTickers(readDirectory);

            //Create portfolio map
            Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap = new HashMap<>();

            System.out.println("Sorting tickers by size");
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

        System.out.println("### Creating S");

        List<TickerInfoDTO> listS = new ArrayList<>(allTickers.stream().limit(sizeS).collect(Collectors.toList()));
        portfolioMap.put(S, listS);

        System.out.println("S Size: " + listS.size());

        System.out.println("Sorting S by BMME and discarding null values");
        List<TickerInfoDTO> listSBME = listS.stream()
                .filter(e -> e.getBmme() != null)
                .sorted(Comparator.comparingDouble(TickerInfoDTO::getBmme))
                .collect(Collectors.toList());

        int sizeSBME = listSBME.size();
        System.out.println("S Size without null BMME values: " + sizeSBME);

        System.out.println("Creating S_L3 (30% -)");
        int percentSL3 = Math.round(30 * sizeSBME / 100);
        List<TickerInfoDTO> listSL3 = new ArrayList<>(listSBME.stream().limit(percentSL3).collect(Collectors.toList()));
        portfolioMap.put(S_L3, listSL3);
        System.out.println("S_L3 size: " + listSL3.size());

        System.out.println("Creating S_NBM4 (40% +-)");
        int percentSNBM4 = Math.round(40 * sizeSBME / 100);
        List<TickerInfoDTO> listSNBM4 = new ArrayList<>(listSBME.stream().skip(percentSL3).limit(percentSNBM4).collect(Collectors.toList()));
        portfolioMap.put(S_NBM4, listSNBM4);
        System.out.println("S_NBM4 size: " + listSNBM4.size());

        System.out.println("Creating S_H3 (30% +)");
        int percentSH3 = (percentSL3 + percentSNBM4);
        List<TickerInfoDTO> listSH3 = new ArrayList<>(listSBME.stream().skip(percentSH3).collect(Collectors.toList()));
        portfolioMap.put(S_H3, listSH3);
        System.out.println("S_H3 size: " + listSH3.size());


//        long sizeB = Math.round(Math.floor((double)tickersTotalSize/2));
//
//        System.out.println("### Creating B");
//        List<TickerInfoDTO> listB = new ArrayList<>(allTickers.stream().limit(sizeB).collect(Collectors.toList()));
//        portfolioMap.put(B, listB);
//
//        System.out.println("S Size: " + listB.size());
//
//        System.out.println("Sorting S by BMME and discarding null values");
//        listB = listB.stream()
//                .filter(e -> e.getBmme() != null)
//                .sorted(Comparator.comparingDouble(TickerInfoDTO::getBmme))
//                .collect(Collectors.toList());
//
//        System.out.println("S Size without null BMME values: " + listB.size());
//
//        System.out.println("Creating S_L3 (30% -)");
//        int percentSL3 = Math.round(30 * sizeB / 100);
//        List<TickerInfoDTO> listBL3 = new ArrayList<>(listB.stream().limit(percentSL3).collect(Collectors.toList()));
//        portfolioMap.put(S_L3, listBL3);
//        System.out.println("S_L3 size: " + listBL3.size());
//
//        System.out.println("Creating S_NBM4 (40% +-)");
//        int percentSNBM4 = Math.round(40 * sizeB / 100);
//        List<TickerInfoDTO> listBNBM4 = new ArrayList<>(listB.stream().skip(percentSL3).limit(percentSNBM4).collect(Collectors.toList()));
//        portfolioMap.put(S_NBM4, listBNBM4);
//        System.out.println("S_NBM4 size: " + listBNBM4.size());
//
//        System.out.println("Creating S_H3 (30% +)");
//        int percentSH3 = (percentSL3 + percentSNBM4);
//        List<TickerInfoDTO> listBH3 = new ArrayList<>(listB.stream().skip(percentSH3).collect(Collectors.toList()));
//        portfolioMap.put(S_H3, listBH3);
//        System.out.println("S_H3 size: " + listBH3.size());
    }

}

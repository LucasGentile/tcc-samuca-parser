package utils;

import dto.TickerInfoDTO;
import enums.PortfolioGroupNamesEnum;
import enums.SortingTypeEnum;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static enums.SortingTypeEnum.*;

public class TickersListUtils {

    public static List<TickerInfoDTO> sortTickersByType(List<TickerInfoDTO> subgroupTickersList, PortfolioGroupNamesEnum listGroupName, SortingTypeEnum sortingType) {
        System.out.println("## Sorting " + listGroupName.name() + " by " + sortingType.name());

        List<TickerInfoDTO> sortedList;

        if(BMME.equals(sortingType)){
            sortedList = subgroupTickersList.stream()
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getBmme))
                    .collect(Collectors.toList());
        } else if(OP.equals(sortingType)){
            sortedList = subgroupTickersList.stream()
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getOp))
                    .collect(Collectors.toList());
        } else if (INV.equals(sortingType)){
            sortedList = subgroupTickersList.stream()
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getInv))
                    .collect(Collectors.toList());
        } else if (SIZE.equals(sortingType)){
            sortedList = subgroupTickersList.stream()
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getSize))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

        System.out.println(listGroupName.name() + " Size without null " + sortingType.name() + " values: " + sortedList.size());

        return sortedList;
    }

    public static List<TickerInfoDTO> sortTickersBySizeWithoutAnyNullValue(List<TickerInfoDTO> allTickersList) {
        System.out.println("## Sorting all tickers by size and discarding all null values");

        List<TickerInfoDTO> sortedListWithoutNullValues = allTickersList.stream()
                .filter(e -> e.getSize() != null)
                .filter(e -> e.getBmme() != null)
                .filter(e -> e.getInv() != null)
                .filter(e -> e.getOp() != null)
                .sorted(Comparator.comparingDouble(TickerInfoDTO::getSize))
                .collect(Collectors.toList());

        System.out.println("All tickers list size without null values: " + sortedListWithoutNullValues.size());

        return sortedListWithoutNullValues;
    }

    public static List<TickerInfoDTO> getDividedList(List<TickerInfoDTO> allTickers, int size, PortfolioGroupNamesEnum groupNamesEnum, boolean isBottom) {

        System.out.println("### Getting list " + groupNamesEnum.name());
        List<TickerInfoDTO> listTickers;
        if(isBottom){
            listTickers = new ArrayList<>(allTickers.stream().limit(size).collect(Collectors.toList()));
        } else {
            listTickers = new ArrayList<>(allTickers.stream().skip(size).collect(Collectors.toList()));
        }
        System.out.println(groupNamesEnum.name() + " Size: " + listTickers.size());

        return listTickers;
    }

    public static List<TickerInfoDTO> createSubGroup(List<TickerInfoDTO> listTickers, Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, PortfolioGroupNamesEnum groupNamesEnum) {

        System.out.println("### Creating " + groupNamesEnum.name());

        portfolioMap.put(groupNamesEnum, listTickers);

        return listTickers;
    }
}

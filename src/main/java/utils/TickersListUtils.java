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

    public static List<TickerInfoDTO> sortTickersByTypeWithoutNull(List<TickerInfoDTO> subgroupTickersList, PortfolioGroupNamesEnum listGroupName, SortingTypeEnum sortingType) {
        System.out.println("## Sorting " + listGroupName.name() + " by " + sortingType.name() + " and discarding null values");

        List<TickerInfoDTO> sortedListWithouNullValues;

        if(BMME.equals(sortingType)){
            sortedListWithouNullValues = subgroupTickersList.stream()
                    .filter(e -> e.getBmme() != null)
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getBmme))
                    .collect(Collectors.toList());
        } else if(OP.equals(sortingType)){
            sortedListWithouNullValues = subgroupTickersList.stream()
                    .filter(e -> e.getOp() != null)
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getOp))
                    .collect(Collectors.toList());
        } else if (INV.equals(sortingType)){
            sortedListWithouNullValues = subgroupTickersList.stream()
                    .filter(e -> e.getInv() != null)
                    .sorted(Comparator.comparingDouble(TickerInfoDTO::getInv))
                    .collect(Collectors.toList());
        } else {
            return null;
        }

        System.out.println(listGroupName.name() + " Size without null " + sortingType.name() + " values: " + sortedListWithouNullValues.size());

        return sortedListWithouNullValues;
    }

    public static List<TickerInfoDTO> getDividedList(List<TickerInfoDTO> allTickers, Map<PortfolioGroupNamesEnum, List<TickerInfoDTO>> portfolioMap, int size, PortfolioGroupNamesEnum groupNamesEnum, boolean isBottom) {

        System.out.println("### Creating " + groupNamesEnum.name());
        List<TickerInfoDTO> listTickers;
        if(isBottom){
            listTickers = new ArrayList<>(allTickers.stream().limit(size).collect(Collectors.toList()));
        } else {
            listTickers = new ArrayList<>(allTickers.stream().skip(size).collect(Collectors.toList()));
        }
        System.out.println(groupNamesEnum.name() + " Size: " + listTickers.size());
        portfolioMap.put(groupNamesEnum, listTickers);

        return listTickers;
    }
}

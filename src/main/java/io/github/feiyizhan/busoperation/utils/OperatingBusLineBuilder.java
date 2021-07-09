package io.github.feiyizhan.busoperation.utils;
import io.github.feiyizhan.busoperation.enums.BusOperatingInLineStateEnum;
import io.github.feiyizhan.busoperation.pojo.*;
import org.springframework.beans.BeanUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 运营线路构建器
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class OperatingBusLineBuilder {

    /**
     * 构建一个运营的公交线路
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param busLine 公交线路
     * @param busConfig 公交配置
     * @return io.github.feiyizhan.busoperation.pojo.OperatingBusLine
     */
    public static OperatingBusLine build(BusLine busLine, BusConfig busConfig){
        OperatingBusLine operatingBusLine = new OperatingBusLine();
        operatingBusLine.setOperatingBusMap(new LinkedHashMap<>());
        BeanUtils.copyProperties(busLine,operatingBusLine);
        //构建运营的公交站
        Map<Integer, OperatingBusStop> operatingBusStopMap = new LinkedHashMap<>();
        busLine.getStopMap().forEach((k,v)->{
            operatingBusStopMap.put(k,OperatingBusStopBuilder.build(v));
        });
        operatingBusLine.setOperatingBusStopMap(operatingBusStopMap);

        Map<Integer,OperatingBus> operatingBusMap = new HashMap<>();
        operatingBusLine.setOperatingBusMap(operatingBusMap);
        //构建参与线路运营的公交车集合
        //构建上行运营的5辆公交车
        int busId = 1;
        for(;busId<=5;busId++){
            operatingBusMap.put(busId,OperatingBusBuilder.build(busId,busLine.getId(), BusOperatingInLineStateEnum.UP,busConfig));
        }
        //构建下行运营的5辆公交车
        for(;busId<=10;busId++){
            operatingBusMap.put(busId,OperatingBusBuilder.build(busId,busLine.getId(), BusOperatingInLineStateEnum.DOWN,busConfig));
        }

        return operatingBusLine;
    }



}

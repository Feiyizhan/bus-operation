package io.github.feiyizhan.busoperation.utils;
import io.github.feiyizhan.busoperation.pojo.BusLine;
import io.github.feiyizhan.busoperation.pojo.BusLineConfig;
import io.github.feiyizhan.busoperation.pojo.BusStop;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 公交站点构建器
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class BusLineBuilder {

    /**
     * 构建一个公交线路
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param id 线路id
     * @param name 线路名称
     * @param busLineConfig 线路配置
     * @return io.github.feiyizhan.busoperation.pojo.BusLine
     */
    public static BusLine build(Integer id,String name,BusLineConfig busLineConfig){
        BusLine line = new BusLine();
        line.setId(id);
        line.setName(name);
        Map<Integer,BusStop> stopMap = new LinkedHashMap<>();
        line.setStopMap(stopMap);
        //因为是demo，不做参数的校验
        Integer[] stopIds = busLineConfig.getStopIds();
        String[] stopCodes = busLineConfig.getStopCodes();
        String[] stopNames = busLineConfig.getStopNames();
        int[] durationOfToNextStops = busLineConfig.getDurationOfToNextStops();
        int[] durationOfToPreviousStops = busLineConfig.getDurationOfToPreviousStops();
        Integer[] idOfNextStops = busLineConfig.getIdOfNextStops();
        Integer[] idOfPreviousStop = busLineConfig.getIdOfPreviousStop();
        //构建站点集合
        for(int i=0;i<stopIds.length;i++){
            BusStop stop = new BusStop();
            stop.setId(stopIds[i]);
            stop.setCode(stopCodes[i]);
            stop.setName(stopNames[i]);
            stop.setDurationOfToNextStop(durationOfToNextStops[i]);
            stop.setDurationOfToPreviousStop(durationOfToPreviousStops[i]);
            stop.setIdOfNextStop(idOfNextStops[i]);
            stop.setIdOfPreviousStop(idOfPreviousStop[i]);
            stopMap.put(stopIds[i],stop);
            if(i==0){
                line.setFirstStop(stop);
            }else if(i==stopIds.length-1){
                line.setLastStop(stop);
            }
        }
        return line;
    }

}

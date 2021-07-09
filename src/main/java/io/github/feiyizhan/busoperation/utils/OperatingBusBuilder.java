package io.github.feiyizhan.busoperation.utils;

import io.github.feiyizhan.busoperation.enums.BusOperatingInLineStateEnum;
import io.github.feiyizhan.busoperation.pojo.BusConfig;
import io.github.feiyizhan.busoperation.pojo.OperatingBus;

import java.util.ArrayList;

/**
 * 运营公交车构建器
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class OperatingBusBuilder {

    /**
     * 构建一个运营的公交车
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param id 公交车id
     * @param busLineId 运营的线路id
     * @param operatingInLineStateEnum 当前的在线路上的状态
     * @param busConfig 公交车的一些配置
     * @return io.github.feiyizhan.busoperation.pojo.OperatingBus
     */
    public static OperatingBus build(Integer id,Integer busLineId,
        BusOperatingInLineStateEnum operatingInLineStateEnum,
        BusConfig busConfig){
        //因为是demo，所以不对参数进行校验
        OperatingBus bus = new OperatingBus();
        bus.setBusLineId(busLineId);
        bus.setName(id.toString());
        bus.setDriversNumber(busConfig.getDriversNumber());
        bus.setConductorsNumber(busConfig.getConductorsNumber());
        bus.setCurrentPassengersNumber(0);
        bus.setInLineState(operatingInLineStateEnum);
        bus.setTotalDurationOfOperating(0L);
        bus.setTotalDurationOfDriving(0L);
        bus.setTotalPassengersNumbers(0L);
        bus.setId(id);
        bus.setMaxCapacity(busConfig.getMaxCapacity());
        bus.setDurationOfToNextStop(0L);
        bus.setCurrentPassengers(new ArrayList<>());
        bus.setCurrentReducePassengersNumber(0);
        bus.setCurrentAddPassengersNumber(0);
        return bus;
    }
}

package io.github.feiyizhan.busoperation;

import io.github.feiyizhan.busoperation.constant.SystemConstant;
import io.github.feiyizhan.busoperation.pojo.BusConfig;
import io.github.feiyizhan.busoperation.pojo.BusLine;
import io.github.feiyizhan.busoperation.pojo.BusLineConfig;
import io.github.feiyizhan.busoperation.pojo.OperatingBusLine;
import io.github.feiyizhan.busoperation.utils.BusLineBuilder;
import io.github.feiyizhan.busoperation.utils.OperatingBusLineBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 模拟运行测试类
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class BusOperationApplicationTests {
    /**
     * 最大运营时间
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private static final long MAX_RUNNING_TIME = 300L;

    public static void main(String[] args) {
        BusLineConfig busLineConfig = getBusLineConfig();
        BusLine busLine = BusLineBuilder.build(1,"测试线路1",busLineConfig);
        BusConfig busConfig = getBusConfig();
        OperatingBusLine operatingBusLine = OperatingBusLineBuilder.build(busLine, busConfig );
        OperatingBusLineManger manger = new OperatingBusLineManger(operatingBusLine);
        manger.run(1*60L, TimeUnit.SECONDS);
    }

    /**
     * 获取线路配置
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param
     * @return io.github.feiyizhan.busoperation.pojo.BusLineConfig
     */
    private static BusLineConfig getBusLineConfig(){
        BusLineConfig busLineConfig = new BusLineConfig();
        busLineConfig.setStopIds(new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15});
        busLineConfig.setStopCodes(new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"});
        busLineConfig.setStopNames(new String[] {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15"});
        busLineConfig.setDurationOfToNextStops(new int[]{5,6,7,8,4,3,6,5,6,7,4,3,6,3,-1});
        busLineConfig.setDurationOfToPreviousStops(new int[] {-1,4,7,5,6,3,4,5,3,7,4,5,4,5,4});
        busLineConfig.setIdOfNextStops(new Integer[]{2,3,4,5,6,7,8,9,10,11,12,13,14,15,null});
        busLineConfig.setIdOfPreviousStop(new Integer[] {null,1,2,3,4,5,6,7,8,9,10,11,12,13,14});
        return busLineConfig;
    }

    /**
     * 获取公交车的配置
     * @author 徐明龙 XuMingLong 2021-06-23
     * @return io.github.feiyizhan.busoperation.pojo.BusConfig
     */
    private static BusConfig getBusConfig(){
        BusConfig busConfig = new BusConfig();
        busConfig.setMaxCapacity(SystemConstant.MAX_CAPACITY);
        busConfig.setDriversNumber(1);
        busConfig.setConductorsNumber(0);
        return busConfig;
    }

}

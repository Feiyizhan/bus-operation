package io.github.feiyizhan.busoperation.utils;

import io.github.feiyizhan.busoperation.pojo.BusLine;
import io.github.feiyizhan.busoperation.pojo.Passenger;

import java.util.ArrayList;
import java.util.List;

/**
 * 乘客构建器
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class PassengerBuilder {
    /**
     * 乘客上车耗时 （秒）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private static final int GET_IN_DURATION = 10;
    /**
     * 乘客下车耗时 （秒）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private static final int GET_OFF_DURATION = 10;


    /**
     * 构建一个乘客
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param busLine
     * @return io.github.feiyizhan.busoperation.pojo.Passenger
     */
    public static Passenger build(Integer id, BusLine busLine){
        Passenger passenger = new Passenger();
        passenger.setId(id);
        passenger.setGetInDuration(GET_IN_DURATION);
        passenger.setGetOffDuration(GET_OFF_DURATION);

        List<Integer> idList = new ArrayList<>(busLine.getStopMap().keySet());
        //随机构建一个起始站和结束站
        passenger.setDestinationStop(CustomRandomUtils.getAndRemoveRandomItem(idList));
        passenger.setFromStop(CustomRandomUtils.getAndRemoveRandomItem(idList));
        return passenger;

    }

}

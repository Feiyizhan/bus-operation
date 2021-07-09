package io.github.feiyizhan.busoperation.utils;

import io.github.feiyizhan.busoperation.pojo.BusStop;
import io.github.feiyizhan.busoperation.pojo.OperatingBusStop;
import io.github.feiyizhan.busoperation.pojo.Passenger;
import org.springframework.beans.BeanUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * 运营的公交站构建器
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class OperatingBusStopBuilder {

    /**
     * 构建一个运营的公交站
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param busStop
     * @return io.github.feiyizhan.busoperation.pojo.OperatingBusStop
     */
    public static OperatingBusStop build(BusStop busStop){
        OperatingBusStop stop = new OperatingBusStop();
        BeanUtils.copyProperties(busStop,stop);
        BlockingQueue<Passenger> waitingInUpPassengers = new LinkedBlockingDeque<>();
        stop.setWaitingInUpPassengers(waitingInUpPassengers);
        BlockingQueue<Passenger> waitingInDownPassengers = new LinkedBlockingDeque<>();
        stop.setWaitingInDownPassengers(waitingInDownPassengers);
        return stop;
    }
}

package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.BlockingQueue;

/**
 * 运营的公交站点
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data @ToString(callSuper = true)@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OperatingBusStop extends BusStop {

    /**
     * 当前在上行站点等候的乘客列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BlockingQueue<Passenger> waitingInUpPassengers;

    /**
     * 当前在下行站点等候的乘客列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BlockingQueue<Passenger> waitingInDownPassengers;
}

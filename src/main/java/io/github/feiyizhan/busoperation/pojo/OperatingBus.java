package io.github.feiyizhan.busoperation.pojo;

import io.github.feiyizhan.busoperation.enums.BusOperatingInLineStateEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 运营的公交车
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data@ToString(callSuper = true)@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OperatingBus extends Bus {

    /**
     * 运营的线路id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer BusLineId;

    /**
     * 司机数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int driversNumber;

    /**
     * 售票员数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int conductorsNumber;

    /**
     * 当前乘客数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int currentPassengersNumber;

    /**
     * 运营线路上的状态（上行/下行）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BusOperatingInLineStateEnum inLineState;

    /**
     * 总运营时间
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long totalDurationOfOperating;

    /**
     * 总行驶时间(秒）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long totalDurationOfDriving;

    /**
     * 总载客人数
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long totalPassengersNumbers;

    /**
     * 当前站id(秒）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer currentStopId;


    /**
     * 下一站id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer nextStopId;

    /**
     * 预计到达下一站的时间(秒）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long durationOfToNextStop;

    /**
     * 当前的用户列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private List<Passenger> currentPassengers;

    /**
     * 本次下车乘客数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int currentReducePassengersNumber;

    /**
     * 本次上车乘客数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int currentAddPassengersNumber;

    /**
     * 本次乘客上下车耗时
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long currentDurationOfPassengers;



}

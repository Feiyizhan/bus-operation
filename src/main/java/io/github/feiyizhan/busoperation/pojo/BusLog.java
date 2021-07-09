package io.github.feiyizhan.busoperation.pojo;

import io.github.feiyizhan.busoperation.enums.BusLogTypeEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 公交车日志
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class BusLog {

    /**
     * 公交车id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer busId;

    /**
     * 公交线路id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer busLineId;
    /**
     * 日志时间
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private LocalDateTime now;
    /**
     * 运行的时长
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private long duration;
    /**
     * 站点的id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer stopId;

    /**
     * 站点编号
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String stopCode;

    /**
     * 站点名称
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String stopName;

    /**
     * 日志类型
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BusLogTypeEnum logType;
    /**
     * 当前乘客数
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int currentPassengersNumber;
    /**
     * 上车的乘客数
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int addPassengersNumber;
    /**
     * 下车的乘客数
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int reducePassengersNumber;

}

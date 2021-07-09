package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

/**
 * 公交车配置
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class BusConfig {

    /**
     * 最大载客数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int maxCapacity;

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

}

package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

/**
 * 公交站配置
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class BusLineConfig {

    /**
     * 站点id列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer[] stopIds;

    /**
     * 站点编号列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String[] stopCodes;

    /**
     * 站点名称列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String[] stopNames;

    /**
     * 到下一个站点的时长（分钟）列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int[] durationOfToNextStops;

    /**
     * 到上一个站点的时长（分钟）列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int[] durationOfToPreviousStops;

    /**
     * 下一站的id 列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer[] idOfNextStops;

    /**
     * 上一个站点id列表
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer[] idOfPreviousStop;


}

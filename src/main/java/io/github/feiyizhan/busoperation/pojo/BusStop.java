package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

/**
 * 公交站
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class BusStop {

    /**
     * 站点id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer id;

    /**
     * 站点编号
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String code;

    /**
     * 站点名称
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String name;
    /**
     * 到下一个站点的时长（分钟）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int durationOfToNextStop;
    /**
     * 到上一个站点的时长（分钟）
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int durationOfToPreviousStop;

    /**
     * 下一站的id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer idOfNextStop;

    /**
     * 上一站的id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer idOfPreviousStop;


}

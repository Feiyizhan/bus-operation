package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

import java.util.Map;

/**
 * 公交线路
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class BusLine {

    /**
     * 线路id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer id;

    /**
     * 线路名称
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String name;

    /**
     * 线路的站点集合
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer,BusStop> stopMap;

    /**
     * 第一个站点
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BusStop firstStop;

    /**
     * 最后一个站点
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private BusStop lastStop;

}

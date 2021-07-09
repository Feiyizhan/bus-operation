package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

/**
 * 乘客
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class Passenger {
    
    /**
     * 乘客id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer id;

    /**
     * 上车耗时
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int getInDuration;

    /**
     * 下车耗时
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int getOffDuration;

    /**
     * 目地站点
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer destinationStop;

    /**
     * 起始站点
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer fromStop;
}

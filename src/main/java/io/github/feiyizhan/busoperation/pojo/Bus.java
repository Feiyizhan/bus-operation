package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;

/**
 * 公交车
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data
public class Bus {

    /**
     * 公交车id
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Integer id;

    /**
     * 公交名称
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private String name;

    /**
     * 最大载客数量
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private int maxCapacity;

}

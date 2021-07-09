package io.github.feiyizhan.busoperation.pojo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Map;

/**
 * 运营的公交线路
 * @author 徐明龙 XuMingLong 2021-06-23
 */
@Data @ToString(callSuper = true)@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class OperatingBusLine extends BusLine {

    /**
     * 在该公交线上运营的公交车集合
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer,OperatingBus> operatingBusMap;


    /**
     * 线路的运营站点集合
     * @author 徐明龙 XuMingLong 2021-06-23
     */
    private Map<Integer,OperatingBusStop> operatingBusStopMap;

}

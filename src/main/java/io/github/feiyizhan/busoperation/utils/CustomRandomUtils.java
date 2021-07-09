package io.github.feiyizhan.busoperation.utils;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author 徐明龙 XuMingLong 2021-06-23
 */
public class CustomRandomUtils {

    /**
     * 获取并移除随机的元素
     * @author 徐明龙 XuMingLong 2021-06-23
     * @param itemList
     * @return java.lang.Integer
     */
    public static Integer getAndRemoveRandomItem(List<Integer> itemList){
        if(CollectionUtils.isEmpty(itemList)){
            return null;
        }
        int randomIndex = org.apache.commons.lang3.RandomUtils.nextInt(0,itemList.size());
        Integer id = itemList.get(randomIndex);
        itemList.remove(randomIndex);
        return id;
    }
}

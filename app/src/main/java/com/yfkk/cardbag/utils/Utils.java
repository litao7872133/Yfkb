package com.yfkk.cardbag.utils;

import java.util.Collection;

/**
 * 常用工具类
 * <p>
 * Created by litao on 2018/1/24.
 */
public class Utils {

    /**
     * 判断集合是否为空
     *
     * @param coll
     * @return 如果为空返回true；否则返回false
     */
    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }


}

package com.cjh.spring.design.decorator.v1;

/**
 * 煎饼接口
 * @author cjh
 * @date 2020/2/10 13:19
 **/
public abstract class Pancakes {
    /**
     * 描述
     */
    abstract String getMessage();

    /**
     * 金额
     */
    abstract int getPrice();
}

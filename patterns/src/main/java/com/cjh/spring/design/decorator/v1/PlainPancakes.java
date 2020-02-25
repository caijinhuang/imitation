package com.cjh.spring.design.decorator.v1;

/**
 * 原味煎饼
 * @author cjh
 * @date 2020/2/10 13:21
 **/
public class PlainPancakes extends Pancakes {

    @Override
    public String getMessage() {
        return "原味煎饼";
    }

    @Override
    public int getPrice() {
        return 5;
    }
}

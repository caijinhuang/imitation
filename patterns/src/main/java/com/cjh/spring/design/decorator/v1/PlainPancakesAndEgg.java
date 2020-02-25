package com.cjh.spring.design.decorator.v1;

/**
 * @author cjh
 * @date 2020/2/10 13:23
 **/
public class PlainPancakesAndEgg extends PancakesDecorator {

    public PlainPancakesAndEgg(Pancakes pancakes) {
        super(pancakes);
    }

    @Override
    public String getMessage() {
        return this.getPancakes().getMessage() + "+加鸡蛋";
    }

    @Override
    public int getPrice() {
        return this.getPancakes().getPrice() + 1;
    }
}

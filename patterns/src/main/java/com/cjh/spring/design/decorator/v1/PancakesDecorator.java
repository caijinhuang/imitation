package com.cjh.spring.design.decorator.v1;

/**
 * 煎饼装饰器
 *
 * @author cjh
 * @date 2020/2/10 13:24
 **/
public abstract class PancakesDecorator extends Pancakes {

    private Pancakes pancakes;

    public Pancakes getPancakes() {
        return pancakes;
    }

    public PancakesDecorator(Pancakes pancakes) {
        this.pancakes = pancakes;
    }


    @Override
    abstract String getMessage();

    @Override
    abstract int getPrice();
}

package com.cjh.spring.design.decorator.v1;

/**
 * @author cjh
 * @date 2020/2/10 13:23
 **/
public class PlainPancakesAndPorkFloss extends PancakesDecorator {
    public PlainPancakesAndPorkFloss(Pancakes pancakes) {
        super(pancakes);
    }

    @Override
    String getMessage() {
        return this.getPancakes().getMessage() + "+加肉松";
    }

    @Override
    int getPrice() {
        return this.getPancakes().getPrice() + 2;
    }
}

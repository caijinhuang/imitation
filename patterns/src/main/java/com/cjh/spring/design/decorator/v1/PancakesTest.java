package com.cjh.spring.design.decorator.v1;

/**
 * @author cjh
 * @date 2020/2/10 13:29
 **/
public class PancakesTest {

    public static void main(String[] args) {
        Pancakes pancakes = new PlainPancakes();
        pancakes = new PlainPancakesAndEgg(pancakes);
        pancakes = new PlainPancakesAndEgg(pancakes);
        pancakes = new PlainPancakesAndSausage(pancakes);
        pancakes = new PlainPancakesAndPorkFloss(pancakes);
        pancakes = new PlainPancakesAndPorkFloss(pancakes);
        System.out.println(pancakes.getMessage());
        System.out.println(pancakes.getPrice());
    }
}

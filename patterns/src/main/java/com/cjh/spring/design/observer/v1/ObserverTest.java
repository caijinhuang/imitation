package com.cjh.spring.design.observer.v1;

/**
 * @author cjh
 * @date 2020/2/10 14:12
 **/
public class ObserverTest {
    public static void main(String[] args) {
        Student student = new Student("小蔡同学");
        student.addObserver(new Teacher("Tom"));
        student.addObserver(new Teacher("TIM"));
        student.post("我作业忘带了！！！");
    }
}

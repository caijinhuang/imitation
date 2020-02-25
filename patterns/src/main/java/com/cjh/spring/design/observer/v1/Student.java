package com.cjh.spring.design.observer.v1;

import java.util.Observable;

/**
 * @author cjh
 * @date 2020/2/10 13:56
 **/
public class Student extends Observable {

    private String name;

    public String getName() {
        return name;
    }

    public Student(String name) {
        this.name = name;
    }

    /**
     * 学生提交作业，是被监听观察的方法。（被观察者）
     * @param content
     */
    public void post(String content) {
        setChanged();
        notifyObservers(new Question(this.name, content));
    }
}

package com.cjh.spring.design.observer.v1;

import java.util.Observable;
import java.util.Observer;

/**
 * @author cjh
 * @date 2020/2/10 14:02
 **/
public class Teacher implements Observer {

    private String name;

    public Teacher(String name) {
        this.name = name;
    }

    /**
     * 观察者，观察到被观察对象有行为时，触发逻辑。
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        Student student = (Student) o;
        Question question = (Question) arg;
        String info = String.format("%s 老师，%s 向您提交了一个问题：\n %s", this.name, student.getName(), question.getContent());
        System.out.println(info);
    }
}

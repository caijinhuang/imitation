package com.cjh.spring.design.observer.v1;

import lombok.Data;

/**
 * 问题
 * @author cjh
 * @date 2020/2/10 13:57
 **/
@Data
public class Question {
    private String name;
    private String content;

    public Question(String name, String content) {
        this.name = name;
        this.content = content;
    }
}

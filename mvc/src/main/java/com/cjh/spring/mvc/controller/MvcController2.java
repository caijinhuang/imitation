package com.cjh.spring.mvc.controller;

import com.cjh.spring.mvc.service.api.TestService;
import com.cjh.spring.mvcframework.annotation.MyAutowired;
import com.cjh.spring.mvcframework.annotation.MyController;
import com.cjh.spring.mvcframework.annotation.MyRequestMapping;

/**
 * @author cjh
 * @date 2020/2/2 13:36
 **/
@MyController
@MyRequestMapping("/mvc2")
public class MvcController2 {

    @MyAutowired("myTest")
    private TestService testService;

    @MyRequestMapping("/name")
    public String doMvc(String name) {
        return testService.getInfo(name);
    }

}

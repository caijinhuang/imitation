package com.cjh.spring.mvc.controller;

import com.cjh.spring.mvc.service.api.TestService;
import com.cjh.spring.mvc.service.impl.TestServiceImpl;
import com.cjh.spring.mvcframework.annotation.MyAutowired;
import com.cjh.spring.mvcframework.annotation.MyController;
import com.cjh.spring.mvcframework.annotation.MyRequestMapping;
import com.cjh.spring.mvcframework.annotation.MyRequestParam;
import lombok.extern.java.Log;

/**
 * @author cjh
 * @date 2020/2/2 13:36
 **/
@Log
@MyController
@MyRequestMapping("/mvc")
public class MvcController {

    @MyAutowired("myTest")
    private TestService testService;

    @MyRequestMapping("/name")
    public String doMvc(@MyRequestParam("name") String name,String age) {
        log.info("age="+age);
        return testService.getInfo(name);
    }

}

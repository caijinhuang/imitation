package com.cjh.spring.mvc.service.impl;

import com.cjh.spring.mvc.service.api.TestService;
import com.cjh.spring.mvcframework.annotation.MyService;

/**
 * @author cjh
 * @date 2020/2/2 13:43
 **/
@MyService("myTest")
public class TestServiceImpl implements TestService {

    @Override
    public String getInfo(String name) {
        return "hello I`m " + name;
    }
}

package com.cjh.spring.mvcframework.v2.servlet;

import com.cjh.spring.mvcframework.annotation.MyRequestParam;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cjh
 * @date 2020/2/2 21:10
 **/
@Data
public class HandlerMapping {
    /**
     * 映射接口的url
     */
    private String url;
    /**
     * 接口处理的方法对象
     */
    private Method method;
    /**
     * 控制器对象
     */
    private Object controller;

    /**
     * method 所有入参类型
     */
    private Class[] parameterTypes;

    /**
     * 参数和数组下标对应关系
     */
    private Map<String, Integer> paramIndexMapping;

    public HandlerMapping(String url, Object controller, Method method) {
        this.url = url;
        this.method = method;
        this.controller = controller;
        parameterTypes = method.getParameterTypes();
        paramIndexMapping = new HashMap<>();
        putParamIndexMapping(method);
    }

    private void putParamIndexMapping(Method method) {
        // 一个方法有多个入参，每个入参可能有多个注解，所以是二维数组
        Annotation[][] pas = method.getParameterAnnotations();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < pas.length; i++) {
            Annotation[] pa = pas[i];
            boolean exist = false;
            // 找指定的注解
            for (Annotation annotation : pa) {
                if (annotation instanceof MyRequestParam) {
                    MyRequestParam myRequestParam = (MyRequestParam) annotation;
                    String paramsName = myRequestParam.value();
                    assertNotExist(paramsName);
                    paramIndexMapping.put(paramsName, i);
                    exist = true;
                    break;
                }
            }
            if (!exist) {
                String paramsName = parameters[i].getName();
                assertNotExist(paramsName);
                paramIndexMapping.put(paramsName, i);
            }
        }
    }

    /**
     * 断言名称不存在
     *
     * @param paramsName
     */
    private void assertNotExist(String paramsName) {
        if (paramIndexMapping.containsKey(paramsName)) {
            String info = String.format("[%s.%s] exist multiple identical keys!!!",
                    method.getDeclaringClass().getName(), method.getName());
            throw new RuntimeException(info);
        }
    }
}

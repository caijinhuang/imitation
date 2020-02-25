package com.cjh.spring.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * @author cjh
 * @date 2020/2/2 13:34
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyService {
    String value() default "";
}

package com.cjh.spring.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * @author cjh
 * @date 2020/2/2 13:32
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyRequestMapping {
    String value() default "";
}

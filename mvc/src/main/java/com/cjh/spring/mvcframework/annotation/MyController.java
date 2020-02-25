package com.cjh.spring.mvcframework.annotation;

import java.lang.annotation.*;

/**
 * @author cjh
 * @date 2020/2/2 13:31
 **/
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyController {

    String value() default "";

}

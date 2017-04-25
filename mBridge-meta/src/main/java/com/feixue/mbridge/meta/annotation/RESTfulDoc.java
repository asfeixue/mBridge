package com.feixue.mbridge.meta.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RESTfulDoc {
    /**
     * 方法的描述
     * @return
     */
    String value() default "";
}

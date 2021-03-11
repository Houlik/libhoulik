package com.houlik.libhoulik.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by houlik on 2018/5/23.
 * 这是映射元件ID绑定
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectViewByID {
    int value();
    int parentId() default 0;
}

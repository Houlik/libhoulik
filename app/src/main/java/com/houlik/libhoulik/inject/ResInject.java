package com.houlik.libhoulik.inject;

import com.lidroid.xutils.view.ResType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by houlik on 2018/5/23.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ResInject {
    int id();
    ResType type();
}

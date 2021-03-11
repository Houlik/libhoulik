package com.houlik.libhoulik.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by houlik on 2018/5/23.
 * 这是映射Activity布局绑定
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentViewByID {
    int value();

    /**
     * @interface
     * @Target 注释用于何处
     * @Retention 什么级别保存该信息
     *      SOURCE
     *      CLASS
     *      RUNTIME
     */

    /**
     * 类
     * Class, interface or enum declaration. TYPE
     */

    /**
     * 成员变量
     * Field declaration. FIELD
     */

    /**
     * Method declaration. METHOD
     */

    /**
     * Parameter declaration. PARAMETER
     */

    /**
     * Constructor declaration. CONSTRUCTOR
     */

    /**
     * Local variable declaration. LOCAL_VARIABLE
     */

    /**
     * Annotation type declaration. ANNOTATION_TYPE
     */

    /**
     * Package declaration. PACKAGE
     */

}

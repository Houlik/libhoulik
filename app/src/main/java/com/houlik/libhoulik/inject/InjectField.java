package com.houlik.libhoulik.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectField {

    String strValue();
    int intValue();
    boolean boolValue();
    byte bValue();
    float fValue();
    double dValue();
    long lValue();

    enum TYPE {STRING, INT, BOOLEAN, BYTE, FLOAT, DOUBLE, LONG, NONE}
    TYPE type() default TYPE.NONE;
}

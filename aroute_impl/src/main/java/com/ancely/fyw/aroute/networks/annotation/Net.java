package com.ancely.fyw.aroute.networks.annotation;

import com.ancely.fyw.aroute.networks.impl.NetType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)// 在运行进通过反身获取
public @interface Net {
    NetType netType() default NetType.AUTO;

    boolean isManThread() default false;
}

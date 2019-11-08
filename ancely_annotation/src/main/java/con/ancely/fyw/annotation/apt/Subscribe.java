package con.ancely.fyw.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import con.ancely.fyw.annotation.apt.bean.ThreadMode;

/*
 *  @项目名：  Componentization
 *  @包名：    con.ancely.fyw.annotation.apt
 *  @文件名:   Subscribe
 *  @创建者:   fanlelong
 *  @创建时间:  2019/11/8 2:43 PM
 *  @描述：    事件总线
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Subscribe {

    ThreadMode threadMode() default ThreadMode.POSTING;

    boolean sticky() default false;

    int priority() default 0;
}

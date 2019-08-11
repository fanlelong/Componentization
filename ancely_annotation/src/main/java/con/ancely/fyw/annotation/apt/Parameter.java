package con.ancely.fyw.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 *  @项目名：  Componentization
 *  @包名：    con.ancely.fyw.annotation.apt
 *  @文件名:   Parameter
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/11 8:34 PM
 *  @描述：    参数的获取
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface Parameter {
    String name() default "";
}

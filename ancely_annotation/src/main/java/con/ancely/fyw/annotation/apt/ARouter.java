package con.ancely.fyw.annotation.apt;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 *  @项目名：  Componentization
 *  @包名：    con.ancely.fyw.annotation.apt
 *  @文件名:   ARouter
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/9 3:51 PM
 *  @描述：    路由注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ARouter {

    String path();

    String group() default "";
}

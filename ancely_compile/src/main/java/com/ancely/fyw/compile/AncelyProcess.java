package com.ancely.fyw.compile;

import com.ancely.fyw.compile.utils.Constance;
import com.ancely.fyw.compile.utils.EmptyUtils;
import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import con.ancely.fyw.annotation.apt.ARouter;
import con.ancely.fyw.annotation.apt.bean.RouteBean;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.compile
 *  @文件名:   AncelyProcess
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/9 3:54 PM
 *  @描述：    TODO
 */
@AutoService(Processor.class)//通过AutoService生成AutoService注解器
@SupportedSourceVersion(value = SourceVersion.RELEASE_7)//jdk版本
@SupportedAnnotationTypes({Constance.AROUTE_ANNOTATION_TYPE})//需要接收的注解类型
@SupportedOptions({Constance.PRODECT_NAME, Constance.APT_PACKAGE})//从build.gradle传过来的参数
public class AncelyProcess extends AbstractProcessor {

    private Elements mElementUtils;
    private Filer mFiler;
    private Types mTypeUtils;
    private Messager mMessager;
    private String mProjectName;
    private String mPackageNameAPT;

    private Map<String, List<RouteBean>> mTempPathMap = new HashMap<>();
    private Map<String, String> mTempGroupMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mFiler = processingEnv.getFiler();
        mTypeUtils = processingEnv.getTypeUtils();
        mMessager = processingEnv.getMessager();

        Map<String, String> options = processingEnvironment.getOptions();
        if (!options.isEmpty()) {
            mProjectName = options.get(Constance.PRODECT_NAME);
            mPackageNameAPT = options.get(Constance.APT_PACKAGE);
            mMessager.printMessage(Diagnostic.Kind.NOTE, mProjectName);
            mMessager.printMessage(Diagnostic.Kind.NOTE, mPackageNameAPT);
        }

        if (EmptyUtils.isEmpty(mProjectName) || EmptyUtils.isEmpty(mPackageNameAPT)) {
            throw new RuntimeException("注解处理器需要的projectNamee和packageNameAPT为null,请在对应build.gradle里配置");
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {

        if (EmptyUtils.isEmpty(set)) return false;

        //获取所有有带ARouter的类
        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ARouter.class);
        if (EmptyUtils.isEmpty(elements)) return false;

        analysisElements(elements);

        //开始创建文件
        TypeElement groupLoadElement = mElementUtils.getTypeElement(Constance.AROUTE_GROUP);
        TypeElement pathLoadElement = mElementUtils.getTypeElement(Constance.AROUTE_PATH);
        try {
            createPathFiels(pathLoadElement);

            createGroupFiels(groupLoadElement, pathLoadElement);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }


    /**
     * 模板
     * public class ARoute$$Path$$app implements ARouteLoadPath {
     *
     * @Override public Map<String, RouteBean> loadPath() {
     * Map<String, RouteBean> pathMap = new HashMap<>();
     * <p>
     * pathMap.put("/app/MainActivity1",
     * RouteBean.create(RouteBean.Type.ACTIVITY,
     * MainActivity.class,
     * "/app/MainActivity1",
     * "app"));
     * return pathMap;
     * }
     * }
     */
    private void createPathFiels(TypeElement pathElement) throws IOException {
        if (EmptyUtils.isEmpty(mTempPathMap)) return;

        //返回值类型为:Map<String, RouteBean> pathMap
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouteBean.class));
        for (Map.Entry<String, List<RouteBean>> entry : mTempPathMap.entrySet()) {
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constance.PATH_METHOD_NAME)//传入的是方法名:loadPath
                    .addAnnotation(Override.class)//方法上的注解类型
                    .addModifiers(Modifier.PUBLIC)//修饰符
                    .returns(methodReturns);//返回类型

            //添加方法里的代码  Map<String, RouteBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouteBean.class),
                    Constance.PATH_METHOD_RETURN,
                    ClassName.get(HashMap.class)
            );

            //添加下面这段代码,可能会有多个
            /**
             * pathMap.put("/app/MainActivity1",
             *      RouteBean.create(RouteBean.Type.ACTIVITY,
             *      MainActivity.class,
             *      "/app/MainActivity1",
             *      "app"));
             */
            for (RouteBean routeBean : entry.getValue()) {
                methodBuilder.addStatement("$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        Constance.PATH_METHOD_RETURN,
                        routeBean.getPath(),
                        ClassName.get(RouteBean.class),
                        ClassName.get(RouteBean.Type.class),
                        routeBean.getType(),
                        ClassName.get((TypeElement) routeBean.getElement()),
                        routeBean.getPath(),
                        routeBean.getGroup()
                );
            }
            methodBuilder.addStatement("return $N", Constance.PATH_METHOD_RETURN);
            String finalName = Constance.PATH_FILE_NAME_PREFIX + entry.getKey();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "APT生成的PATH类名为: " + mPackageNameAPT + "." + finalName);


            TypeSpec typeSpec = TypeSpec.classBuilder(finalName)//生成的类名
                    .addSuperinterface(ClassName.get(pathElement))//实现的接口
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(methodBuilder.build())//添加的方法
                    .build();

            JavaFile.builder(mPackageNameAPT, typeSpec).build().writeTo(mFiler);
            mTempGroupMap.put(entry.getKey(), finalName);
        }

    }

    //    public class ARoute$$Group$$order implements ARouteLoadGroup {
//        @Override
//        public Map<String, Class<? extends ARouteLoadPath>> loadGroup() {
//            Map<String,Class<? extends ARouteLoadPath>> groupMap = new HashMap<>();
//            groupMap.put("order",ARoute$$Path$$order.class);
//            return groupMap;
//        }
//    }
    //如上
    private void createGroupFiels(TypeElement groupLoadElement, TypeElement pathLoadElement) throws IOException {
        if (EmptyUtils.isEmpty(mTempGroupMap)) return;
        TypeName returnType = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadElement))));

        //Map<String,Class<? extends ARouteLoadPath>>

        MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(Constance.GROUP_METHOD_NAME)//方法名
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(returnType);
//            Map<String,Class<? extends ARouteLoadPath>> groupMap = new HashMap<>();
        methodBuild.addStatement("$T<$T,$T> $L = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathLoadElement))),
                Constance.GROUP_METHOD_RETURN,
                ClassName.get(HashMap.class)
        );
        //groupMap.put("order",ARoute$$Path$$order.class);
        for (Map.Entry<String, String> entry : mTempGroupMap.entrySet()) {
            // entry.value(): RouteBean.create(RouteBean.Type.ACTIVITY,MainActivity.class,"/app/MainActivity","app")


            //groupMap.put("order",ARoute$$Path$$order.class);
            methodBuild.addStatement("$L.put($S,$T.class)",
                    Constance.GROUP_METHOD_RETURN,
                    entry.getKey(),
                    ClassName.get(mPackageNameAPT, entry.getValue())
            );
        }
// return groupMap;
        methodBuild.addStatement("return $N", Constance.GROUP_METHOD_RETURN);

        //创建类
        String finalName = Constance.GROUP_FILE_NAME_PREFIX + mProjectName;
        mMessager.printMessage(Diagnostic.Kind.NOTE, "APT生成的GROUP类名为: " + mPackageNameAPT + "." + finalName);
        TypeSpec typeSpec = TypeSpec.classBuilder(finalName)//类名
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ClassName.get(groupLoadElement))
                .addMethod(methodBuild.build())
                .build();
        JavaFile.builder(mPackageNameAPT, typeSpec).build().writeTo(mFiler);

    }

    private void analysisElements(Set<? extends Element> elements) {

        //获取Activity类型
        TypeElement activityType = mElementUtils.getTypeElement(Constance.ACTIVITY_PATH);

        //获取Activity类信息
        TypeMirror activityMirror = activityType.asType();

        for (Element element : elements) {

            //获取每个元素的类信息
            TypeMirror elementMirror = element.asType();
            mMessager.printMessage(Diagnostic.Kind.NOTE, "遍历元素的信息为: " + elementMirror.toString());

            //获取每个类上的ARoute注解
            ARouter aRouter = element.getAnnotation(ARouter.class);

            RouteBean routeBean = new RouteBean.Builder()
                    .setGroup(aRouter.group())
                    .setPath(aRouter.path())
                    .setElement(element)
                    .build();
            //说明ARoute的注解仅仅只能用于类之上并且类型跟Activity相关 规范不能乱使用
            if (mTypeUtils.isSubtype(elementMirror, activityMirror)) {
                routeBean.setType(RouteBean.Type.ACTIVITY);
            } else {
                throw new RuntimeException("Aroute只限于继承Activity相关类之上");
            }
            // 为tempPathMap附值
            addToPathMap(routeBean);
        }
    }

    private void addToPathMap(RouteBean routeBean) {
        //开始对ARouter的path的值进行检测
        if (checkARouterPath(routeBean)) {//检测合法
            mMessager.printMessage(Diagnostic.Kind.NOTE, "RouteBean: " + routeBean.toString());

            List<RouteBean> routeBeans = mTempPathMap.get(routeBean.getGroup());
            if (EmptyUtils.isEmpty(routeBeans)) {
                routeBeans = new ArrayList<>();
                routeBeans.add(routeBean);
                mTempPathMap.put(routeBean.getGroup(), routeBeans);
            } else {
                boolean isHave = false;
                for (RouteBean bean : routeBeans) {
                    if (routeBean.getPath().equalsIgnoreCase(bean.getPath())) {
                        isHave = true;
                        break;
                    }
                }
                if (!isHave) {
                    routeBeans.add(routeBean);
                    mTempPathMap.put(routeBean.getGroup(), routeBeans);
                }
            }
        }
    }

    private boolean checkARouterPath(RouteBean routeBean) {
        String routerPath = routeBean.getPath();
        String routerGroup = routeBean.getGroup();

        if (EmptyUtils.isEmpty(routerPath) || !routerPath.startsWith("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "请检查传入的path是否按照如下path1: /app/MainActivity");
            return false;
        }

        if (routerPath.lastIndexOf("/") == 0) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "请检查传入的path是否按照如下path2: /app/MainActivity");
            return false;
        }
        String finalGroup = routerPath.substring(1, routerPath.indexOf("/", 1));

        if (finalGroup.contains("/")) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "请检查传入的path是否按照如下path3: /app/MainActivity");
            return false;
        }

        //@ARoute中有group值
        if (!EmptyUtils.isEmpty(routerGroup) && !routerGroup.equals(mProjectName)) {
            mMessager.printMessage(Diagnostic.Kind.ERROR, "@ARouter中的group名必须和当前子moudle相同");
            return false;
        } else {
            routeBean.setGroup(mProjectName);
        }
        return true;
    }
}

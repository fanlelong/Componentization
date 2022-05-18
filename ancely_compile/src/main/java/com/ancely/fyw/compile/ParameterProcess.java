package com.ancely.fyw.compile;

import com.ancely.fyw.annotation.apt.Parameter;
import com.ancely.fyw.compile.utils.Constance;
import com.ancely.fyw.compile.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

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
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/*
 *  @项目名：  Componentization
 *  @包名：    com.ancely.fyw.compile
 *  @文件名:   ParameterProcess
 *  @创建者:   fanlelong
 *  @创建时间:  2019/8/11 8:35 PM
 *  @描述：    TODO
 */
//@AutoService(Processor.class)//通过AutoService生成AutoService注解器
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)//jdk版本
@SupportedAnnotationTypes({Constance.PARAMETER_ANNOTATION_TYPE})//需要接收的注解类型
public class ParameterProcess extends AbstractProcessor {

    private Elements mElementUtils;

    //类信息工具类
    private Types mTypesUtils;

    //用来日志输出的工具类 如警告等
    private Messager mMessager;

    private Filer mFiler;//文件生成器


    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();
    private TypeMirror mCallTypeMirror;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mElementUtils = processingEnv.getElementUtils();
        mTypesUtils = processingEnv.getTypeUtils();
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
        mCallTypeMirror = mElementUtils.getTypeElement(Constance.Call_PATH).asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (EmptyUtils.isEmpty(set)) return false;

        Set<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(Parameter.class);
        if (EmptyUtils.isEmpty(elements)) return false;

        pareseParameterElements(elements);

        try {
            createParameterFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


    /**
     * public class MainActivity$$Parameter implements ParameterLoad {
     *
     * @throws IOException
     * @Override public void loadParameter(Object target) {
     * MainActivity t = (MainActivity) target;
     * t. mDrawable = (OrderDrawable)RouterManager.getInstance().build("/order/getDrawable").navigation(t);
     * }
     * }
     */
    private void createParameterFile() throws IOException {
        if (EmptyUtils.isEmpty(tempParameterMap)) return;

        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT, Constance.PARAMETER_LOAD).build();
        for (Map.Entry<TypeElement, List<Element>> entry : tempParameterMap.entrySet()) {
            TypeElement typeElement = entry.getKey();//获取到当前成功变量所在的类信息


            //@Override
            //public void loadParameter(Object target)
            MethodSpec.Builder methodBuild = MethodSpec.methodBuilder(Constance.PARAMETER_METHOD_NAME)//方法名
                    .addModifiers(Modifier.PUBLIC)
                    .addAnnotation(Override.class)
                    .addParameter(parameterSpec);//方法体的参数

            //添加第一行代码 如MainActivity t = (MainActivity) target;
            methodBuild.addStatement("$T t = ($T) $L",
                    ClassName.get(typeElement),
                    ClassName.get(typeElement),
                    Constance.PARAMETER_LOAD);

            //获取类名
            ClassName className = ClassName.get(typeElement);
            // t. mDrawable = (OrderDrawable)RouterManager.getInstance().build("/order/getDrawable").navigation(t);
            for (Element element : entry.getValue()) {
                buildStatement(element, methodBuild);
            }
            //最终生成的类文件名 (类名$$Parameter)
            String finalName = typeElement.getSimpleName() + Constance.PARAMETER_FILE_NAME;
            mMessager.printMessage(Diagnostic.Kind.NOTE, "APT生成的Parameter类名为: " + className.packageName() + "." + finalName);

            TypeElement parameterType = mElementUtils.getTypeElement(Constance.PARAMETER_PATH);
            TypeSpec typeSpec = TypeSpec.classBuilder(finalName)//类名如Handler
                    .addModifiers(Modifier.PUBLIC)
                    .addSuperinterface(ClassName.get(parameterType))
                    .addMethod(methodBuild.build())
                    .build();
            JavaFile.builder(className.packageName(), typeSpec).build().writeTo(mFiler);
        }

    }

    private void buildStatement(Element element, MethodSpec.Builder methodBuild) {
        TypeMirror typeMirror = element.asType();//获取到成功变量的类型 比如int string Handler这样的
        //获取成员变量的变量名: 如 :  String userName的userName
        String fileName = element.getSimpleName().toString();

        //成员变量注解的值
        String annotationValue = element.getAnnotation(Parameter.class).name();

        //判断注解的值是否为空 为空则附为属性的值
        annotationValue = EmptyUtils.isEmpty(annotationValue) ? fileName : annotationValue;
        //获取Typekind枚举类型的序列号 比如int booler 对应的int 值 Handler 对应的Hander类型
        int type = typeMirror.getKind().ordinal();
        //t.age
        String finalValue = "t." + fileName;
        String methContent = finalValue + " = t.getIntent().";
        mMessager.printMessage(Diagnostic.Kind.NOTE, "TypeKind.ARRAY.ordinal(): " + type);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "TypeKind.ARRAY.ordinal(): " + typeMirror.toString());
        if (type == TypeKind.INT.ordinal()) {
            methContent += "getIntExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.BOOLEAN.ordinal()) {
            //getBooleanExtra("sdfsdf",false)
            methContent += "getBooleanExtra($S, " + finalValue + ")";
        } else if (type == TypeKind.ARRAY.ordinal()) {
            //getBooleanExtra("sdfsdf",false)
            methContent += "getStringArrayExtra($S)";
        } else {
            if (typeMirror.toString().equalsIgnoreCase(Constance.STRING)) {
                methContent += "getStringExtra($S)";
            } else if (typeMirror.toString().contains(Constance.ARRAYLIST)) {
                methContent += "getStringArrayListExtra($S)";
            } else if (mTypesUtils.isSubtype(typeMirror, mCallTypeMirror)) {//注解在一个Call实现类的接口之上

                //  t.mDrawable = (OrderDrawable) RouterManager.getInstance().build("/order/getDrawable").navigation(t);
                methContent = finalValue + " = ($T)$T.getInstance().build($S).navigation(t)";
                methodBuild.addStatement(methContent,
                        TypeName.get(typeMirror),
                        ClassName.get(mElementUtils.getTypeElement(Constance.ROUTER_MANAGER)),
                        annotationValue
                );
                return;
            } else {
                //t.mUserManager = (UserManager) t.getIntent().getSerializableExtra("array");
                methContent = finalValue + "= ($T) t.getIntent().getSerializableExtra($S)";
                methodBuild.addStatement(methContent, TypeName.get(typeMirror), annotationValue);
                return;
            }
        }
        methodBuild.addStatement(methContent, annotationValue);
    }


    private void pareseParameterElements(Set<? extends Element> elements) {
        for (Element element : elements) {
            //注解的属性,父节点是类节点
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();


            List<Element> elementList = tempParameterMap.get(typeElement);

            if (EmptyUtils.isEmpty(elementList)) {
                elementList = new ArrayList<>();
                elementList.add(element);
                tempParameterMap.put(typeElement, elementList);
            } else {
                elementList.add(element);
            }
        }
    }
}

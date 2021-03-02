package com.cn.image.common.tools;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;

/**
 * 
 * @author 
 *
 */
public class CodeGenerator {

    private static final String PACKAGE_NAME = "com.cn.image.controller";
    private static final String CLASS_NAME = "com.cn.image.athena.AthenaType";

    public static void main(String[] args) {
        String[] packages = new String[1];
        packages[0] = PACKAGE_NAME;
        String header = "public class AthenaType {\n" + "\n"
                + "    private static final Map<String, String> TYPE_MAP = new HashMap<String, String>() {\n"
                + "        {";
        String tail = "}\n" + "    };\n" + "\n" + "    public static String get(String key) {\n"
                + "        return TYPE_MAP.get(key);\n" + "    }\n"
                + "public static final String MAX = \"%d\";\n" + "}";

        //初始值
        int code = 231000;
        try {
            Class<?> k = Class.forName(CLASS_NAME);
            code = Integer.parseInt(String.valueOf(k.getDeclaredField("MAX").get(null)));
        } catch (ClassNotFoundException e) {
        } catch (NoSuchFieldException e) {
        } catch (IllegalAccessException e) {
        }
        StringBuilder sb = new StringBuilder();
        @SuppressWarnings("unchecked")
        LoadPackageClasses loadPackageClasses = new LoadPackageClasses(packages,
                RestController.class, Controller.class);
        try {
            Set<Class<?>> classSet = loadPackageClasses.getClassSet();
            for (Class<?> klazz : classSet) {
                Method[] methods = klazz.getDeclaredMethods();
                for (Method method : methods) {
                    if (method.isAnnotationPresent(RequestMapping.class)) {
                        String signature = MethodSignature.generateSignature(method);
                        if (get(signature) == null) {
                            sb.append("put(").append("\"").append(signature).append("\"")
                                    .append(",").append("\"").append(code++).append("\"")
                                    .append(");\n");
                        }
                    }
                }
            }
            System.out.println(header + sb.toString() + String.format(tail, code));
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private static String get(String key) {
        String value = null;
        try {
            Class<?> k = Class.forName(CLASS_NAME);
            Method method = k.getMethod("get", String.class);
            value = (String) method.invoke(null, key);
        } catch (ClassNotFoundException e) {
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return value;
    }
}

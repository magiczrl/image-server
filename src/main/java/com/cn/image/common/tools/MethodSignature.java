package com.cn.image.common.tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by toor on 6/12/17.
 */
public class MethodSignature {

    /**
     * 由method计算签名
     */
    public static String generateSignature(Method method) {
        if (method == null) {
            return "";
        }
        return getInternalName(method.getDeclaringClass())
                + generateSignature(method.getName(), method.getParameterTypes());
    }

    /**
     * 根据方法名、参数列表计算签名
     */
    public static String generateSignature(String methodName, Class<?>... parameterTypes) {
        if (methodName == null) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append('(');
        if (parameterTypes != null) {
            for (Class<?> parameterType : parameterTypes) {
                result.append(getInternalName(parameterType));
            }
        }
        result.append(')');
        return result.toString();
    }

    /**
     * 
     * @param c
     * @return
     */
    public static String getInternalName(Class<?> c) {
        if (c.isArray()) {
            return '[' + getInternalName(c.getComponentType());
        } else if (c == boolean.class) {
            return "Z";
        } else if (c == byte.class) {
            return "B";
        } else if (c == short.class) {
            return "S";
        } else if (c == int.class) {
            return "I";
        } else if (c == long.class) {
            return "J";
        } else if (c == float.class) {
            return "F";
        } else if (c == double.class) {
            return "D";
        } else if (c == char.class) {
            return "C";
        } else if (c == void.class) {
            return "V";
        } else {
            return 'L' + c.getName().replace('.', '/') + ';';
        }
    }

    /**
     * 
     * @param clazz
     * @return
     */
    public static List<String> getMethodSignatures(Class<?> clazz) {
        List<String> signatures = new ArrayList<>();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            signatures.add(MethodSignature.generateSignature(method));
        }
        return signatures;
    }
}

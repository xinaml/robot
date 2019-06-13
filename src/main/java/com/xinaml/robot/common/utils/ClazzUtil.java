package com.xinaml.robot.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ClazzUtil {
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz, int index) {

        Type genType = clazz.getGenericSuperclass();// 得到泛型父类

        // 如果没有实现ParameterizedType接口，即不支持泛型，直接返回Object.class
        if (!(genType instanceof ParameterizedType)) {

            return Object.class;
        }

        // 返回表示此类型实际类型参数的Type对象的数组,数组里放的都是对应类型的Class, 如BuyerServiceBean extends
        // DaoSupport<Buyer,Contact>就返回Buyer和Contact类型
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {

            throw new RuntimeException("你输入的索引"
                    + (index < 0 ? "不能小于0" : "超出了参数的总数！"));
        }
        if (!(params[index] instanceof Class)) {

            return Object.class;
        }
        return (Class) params[index];
    }

    /**
     * 通过反射,获得指定类的父类的第一个泛型参数的实际类型. 如DaoSupport<Buyer>
     *
     * @param clazz
     *            clazz 需要反射的类,该类必须继承泛型父类
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     *         <code>Object.class</code>
     */
    @SuppressWarnings("unchecked")
    public static Class getSuperClassGenricType(Class clazz) {

        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得方法返回值泛型参数的实际类型. 如: public Map<String, Buyer> getNames(){}
     *
     * @param method 方法
     * @param index  泛型参数所在索引,从0开始.
     * @return 泛型参数的实际类型, 如果没有实现ParameterizedType接口，即不支持泛型，所以直接返回
     *         <code>Object.class</code>
     */
    @SuppressWarnings("unchecked")
    public static Class getMethodGenericReturnType(Method method, int index) {

        Type returnType = method.getGenericReturnType();

        if (returnType instanceof ParameterizedType) {

            ParameterizedType type = (ParameterizedType) returnType;
            Type[] typeArguments = type.getActualTypeArguments();

            if (index >= typeArguments.length || index < 0) {

                throw new RuntimeException("你输入的索引"
                        + (index < 0 ? "不能小于0" : "超出了参数的总数！"));
            }
            return (Class) typeArguments[index];
        }
        return Object.class;
    }


    public static List<Field> getFields(Class clazz) {
        List<Field> fields = new ArrayList<>();
        while (null != clazz) { //数据源类所有属性（包括父类）
            fields.addAll(Arrays.asList(clazz.getDeclaredFields())); //源对象属性
            clazz = clazz.getSuperclass();
            if (Object.class.equals(clazz) || null == clazz) {
                break;
            }
        }
        return fields;
    }

    /**
     * 获取某个类的所有方法,包括父类
     *
     * @param clazz
     * @return
     */
    public static List<Method> getMethods(Class clazz) {
        List<Method> methods = new ArrayList<>();
        while (null != clazz) { //目标类所有属性（包括父类）
            methods.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            clazz = clazz.getSuperclass();
            if (Object.class.equals(clazz) || null == clazz) {
                break;
            }
        }
        return methods;
    }

}

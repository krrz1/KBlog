package com.krrz.utils;

import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {
    private BeanCopyUtils(){}

    public static <V>V copyBean(Object source,Class<V> clazz) {
        V result = null;
        try {
            //创建目标对象
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream().map(o -> copyBean(o, clazz)).collect(Collectors.toList());
    }
}

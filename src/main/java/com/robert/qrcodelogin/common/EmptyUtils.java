package com.robert.qrcodelogin.common;

import java.util.Collection;
import java.util.Map;

public class EmptyUtils {
    /**
     * 是否为null或不可见字符的字符串
     * @author zhangyapo
     * @date 2018-09-10
     * @param source
     * @return
     */
    public static boolean isBlank(String source){
        return source==null || source.trim().length()==0;
    }
    public static boolean isNotBlank(String source){
        return !isBlank(source);
    }
    /**
     * 是否为null或空字符串
     * @author zhangyapo
     * @date 2018-09-10
     * @param source
     * @return
     */
    public static boolean isEmpty(String source){
        return source==null || source.length()==0;
    }
    public static boolean isNotEmpty(String source){
        return !isEmpty(source);
    }
    public static <T> boolean isEmpty(Collection<T> list){
        return list==null || list.size()==0;
    }
    public static <T> boolean isNotEmpty(Collection<T> list){
        return !isEmpty(list);
    }
    public static <T,K> boolean isEmpty(Map<T,K> map){
        return map==null || map.size()==0;
    }
    public static <T,K> boolean isNotEmpty(Map<T,K> map){
        return !isEmpty(map);
    }
    public static <T> boolean isEmpty(T[] arrays){
        return arrays==null || arrays.length==0;
    }
    public static <T> boolean isNotEmpty(T[] arrays){
        return !isEmpty(arrays);
    }
}

package com.example.camunda.utils;


import com.example.camunda.exception.ResultException;

import java.util.Collection;
import java.util.Objects;

public class AssertUtils {

    public static void isTrue(boolean flag,String msg){
        if(flag){
            throw new ResultException(msg);
        }
    }

    public static void isFalse(boolean flag,String msg){
        if(!flag){
            throw new ResultException(msg);
        }
    }

    public static void isNull(Object obj,String msg){
        if(Objects.isNull(obj)){
            throw new ResultException(msg);
        }
    }
    public static void isNotNull(Object obj,String msg){
        if(Objects.nonNull(obj)){
            throw new ResultException(msg);
        }
    }

    public static <T> void isNotEmpty(Collection<T> collection, String msg) {
        if(collection!=null&&collection.size()>0){
            throw new ResultException(msg);
        }
    }

    public static <T> void isEmpty(Collection<T> collection, String msg) {
        if(collection==null||collection.size()==0){
            throw new ResultException(msg);
        }
    }

    public static boolean isGreaterZero(Long num){
        return Objects.nonNull(num)&&num.compareTo(0L)>0;
    }

    public static boolean isNotGreaterZero(Long num){
        return Objects.isNull(num)||num.compareTo(0L)<=0;
    }

}

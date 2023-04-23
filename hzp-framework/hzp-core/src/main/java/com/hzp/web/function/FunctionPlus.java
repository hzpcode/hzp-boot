package com.hzp.web.function;
/**
* 函数式接口扩展
* 
* @author   yxy 
* @date     2020/09/16 17:50
*
**/

@FunctionalInterface
public interface FunctionPlus<T, R> {

    R apply(T t) throws Exception;


}

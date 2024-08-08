package com.nft.cn.annotation;

@FunctionalInterface
public
interface StatFunction<T> {

    T stat(T t1, T t2);

}
package com.lzp.domain;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author lizhengpeng
 * @create 2021/2/19 - 10:10
 * @describe
 */
//在企业中所有对象都要序列化，springboot
public class User implements Serializable {
    private String name;
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public User(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public User() {

    }

}

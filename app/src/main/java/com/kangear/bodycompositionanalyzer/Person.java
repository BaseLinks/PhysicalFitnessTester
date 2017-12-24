package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

/**
 * Created by tony on 17-12-24.
 */

public class Person {
    int fingerId;
    String id;
    int age;
    int height;
    // 性别
    int gender;
    int weight;
    public static final int GENDER_MALE   = 1;
    public static final int GENDER_FEMALE = 2;

    public int getFingerId() {
        return fingerId;
    }

    public void setFingerId(int fingerId) {
        this.fingerId = fingerId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    // You can add those functions as LiveTemplate !
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Person fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Person.class);
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Person{" +
                "fingerId=" + fingerId +
                ", id='" + id + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", gender=" + gender +
                ", weight=" + weight +
                '}';
    }
}

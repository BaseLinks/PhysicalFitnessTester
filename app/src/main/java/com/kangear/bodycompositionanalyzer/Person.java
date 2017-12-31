package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

/**
 * Created by tony on 17-12-24.
 */

public class Person {
    int fingerId;
    String id;
    int age;
    float height;
    // 性别
    String gender;
    float weight;
    public static final String GENDER_MALE   = "男";
    public static final String GENDER_FEMALE = "女";

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

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
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

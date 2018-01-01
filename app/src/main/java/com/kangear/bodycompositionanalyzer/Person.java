package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tony on 17-12-24.
 */
@Table(name = "person", onCreated = "CREATE UNIQUE INDEX index_name ON person(id,fingerId)")
public class Person {
    @Column(name = "uid", isId = true)
    private int uid;
    @Column(name = "id")
    private String id;
    @Column(name = "fingerId")
    private int fingerId;
    @Column(name = "age")
    private int age;
    @Column(name = "height")
    private float height;
    @Column(name = "gender")
    private String gender;
    @Column(name = "weight")
    private float weight;
    @Column(name = "date")
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static final String GENDER_MALE   = "男";
    public static final String GENDER_FEMALE = "女";

    public Person(String id, String gender, int age) {
        this.id = id;
        this.gender = gender;
        this.age = age;
    }

    public Person() {

    }

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

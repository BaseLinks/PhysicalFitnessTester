package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tony on 18-1-1.
 */

@Table(name = "record")
public class Record {
    public static final String ID = "id";
    public static final String PERSON_ID = "personId";

    /**
     * database table's id
     */
    @Column(name = ID, isId = true)
    private int id;
    @Column(name = PERSON_ID)
    private int personId;    // 外键表id
    /**
     * UI's `ID`
     */
    @Column(name = "name")
    private String name;
    @Column(name = "age")
    private int age;
    @Column(name = "height")
    private float height;
    @Column(name = "gender")
    private String gender;
    @Column(name = "weight")
    private float weight;
    /**
     * test's date
     */
    @Column(name = "date")
    private String date;

    public Record() {

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

    public Record(String date) {
        this.date = date;
    }

    public Record(int id, int personId, String name, int age, float height, String gender, float weight, String date) {
        this.id = id;
        this.personId = personId;
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.weight = weight;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPerson(Person p) {
        this.age = p.getAge();
        this.gender = p.getGender();
        this.personId = p.getId();
        this.height = p.getHeight();
        this.name = p.getName();
    }

    // You can add those functions as LiveTemplate !
    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static Record fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, Record.class);
    }

    @Override
    public String toString() {
        return "Record{" +
                "id=" + id +
                ", personId=" + personId +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", gender='" + gender + '\'' +
                ", weight=" + weight +
                ", date='" + date + '\'' +
                '}';
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}
package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by tony on 18-1-1.
 */

@Table(name = "record")
public class Record {
    public static final String ID = "id";
    public static final String DB_COL_PERSON_ID = "personId";
    public static final String DB_COL_TIME = "time";

    /**
     * database table's id
     */
    @Column(name = ID, isId = true)
    private int id;
    @Column(name = DB_COL_PERSON_ID)
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
    @Column(name = "img")
    private String img;
    @Column(name = "time")
    private long time;
    @Column(name = "data")
    private byte[] data;
    private BodyComposition bodyComposition;

    public Record() {

    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
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

    public Record(int id, int personId, String name, int age, float height, String gender, float weight, long time) {
        this.id = id;
        this.personId = personId;
        this.name = name;
        this.age = age;
        this.height = height;
        this.gender = gender;
        this.weight = weight;
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getDateFormatted(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return formatter.format(getTime());
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
                ", img='" + img + '\'' +
                ", time=" + time +
                ", data=" + Arrays.toString(data) +
                ", bodyComposition=" + bodyComposition +
                '}';
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public BodyComposition getBodyComposition() {
        if (bodyComposition == null)
            bodyComposition = new BodyComposition(getData());
        return bodyComposition;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setBodyComposition(BodyComposition bodyComposition) {
        this.bodyComposition = bodyComposition;
    }
}

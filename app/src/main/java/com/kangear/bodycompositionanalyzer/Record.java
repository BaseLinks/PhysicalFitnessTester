package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tony on 18-1-1.
 */

@Table(name = "record")
public class Record {
    @Column(name = "id", isId = true)
    private int id;
    @Column(name = "personId" /*, property = "UNIQUE"//如果是一对一加上唯一约束*/)
    private int personId;    // 外键表id
    private Person person;
    @Column(name = "date")
    private String date;

    public Record(Person person, String date) {
        this.person = person;
        this.date = date;
    }

    public Record() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }
}

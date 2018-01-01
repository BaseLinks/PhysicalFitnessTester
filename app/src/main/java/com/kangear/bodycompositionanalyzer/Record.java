package com.kangear.bodycompositionanalyzer;

import com.google.gson.Gson;

/**
 * Created by tony on 18-1-1.
 */

public class Record {
    Person person;
    String date;

    public Record(Person person, String date) {
        this.person = person;
        this.date = date;
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
}

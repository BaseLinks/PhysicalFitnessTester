package com.kangear.bodycompositionanalyzer;

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
}

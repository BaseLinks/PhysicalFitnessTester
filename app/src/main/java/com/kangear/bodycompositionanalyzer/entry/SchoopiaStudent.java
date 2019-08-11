package com.kangear.bodycompositionanalyzer.entry;

import com.kangear.bodycompositionanalyzer.Person;

public class SchoopiaStudent {
    /**
     * uid : 11834201801001
     * age : 19
     * height : 180
     * gender : 1
     */

    private String uid;
    private String age;
    private String height;
    private String gender;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public static Person toPerson(SchoopiaStudent student) {
        Person mPerson = new Person();
        if (student == null) {
            return mPerson;
        }

        mPerson.setAge(Integer.valueOf(student.getAge()));
        mPerson.setName(student.getUid());
        mPerson.setHeight(Float.valueOf(student.getHeight()));
        mPerson.setGender(student.getGender());

        return mPerson;
    }
}

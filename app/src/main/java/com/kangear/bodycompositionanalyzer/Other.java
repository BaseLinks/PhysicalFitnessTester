package com.kangear.bodycompositionanalyzer;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by tony on 17-12-24.
 */
@Table(name = "other")
public class Other {
    public static final String OTHER_NAME         = "name";
    public static final String OTHER_STRING_VALUE = "str_value";
    public static final String OTHER_NAME_COMPANY = "company";
    public static final String OTHER_NAME_NUMBER  = "number";

    @Column(name = "id", isId = true)
    private int id;
    @Column(name = OTHER_NAME)
    private String name;
    @Column(name = OTHER_STRING_VALUE)
    private String strValue;

    public int getId() {
        return id;
    }

    public Other setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrValue() {
        return strValue;
    }

    public void setStrValue(String strValue) {
        this.strValue = strValue;
    }

    @Override
    public String toString() {
        return "Other{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", strValue='" + strValue + '\'' +
                '}';
    }

    public Other(String name, String strValue) {
        this.name = name;
        this.strValue = strValue;
    }

    public Other() {

    }
}

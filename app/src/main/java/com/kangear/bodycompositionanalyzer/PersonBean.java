package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;

/**
 * Created by tony on 18-1-1.
 */

public class PersonBean {
    private static final String TAG = "PersonBean";
    private volatile static PersonBean singleton = null;

    public PersonBean(Context context) {
        init();
    }

    // 请使用Application context
    public static PersonBean getInstance(Context context) {
        if (singleton == null) {
            synchronized (PersonBean.class) {
                if (singleton == null) {
                    singleton = new PersonBean(context.getApplicationContext());
                }
            }
        }
        return singleton;
    }

    /**
     * 读取所有数据
     */
    private void init() {
//        try {
//            mRecords = WelcomeActivity.getDB().selector(Record.class).findAll();
//            if (mRecords != null)
//                Log.i(TAG, "mRecords.size(): " + mRecords.size());
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }

    /**
     * @param pageNumber 当前页面
     * @param itemsPerPage  页面长度
     * @return
     */
    public List<Record>  getRecordList(int pageNumber, int itemsPerPage) {
        List<Record> records = new ArrayList<>();
        Record record;
        if (pageNumber < 1 || itemsPerPage < 1) {
            return records;
        }
        pageNumber --; // 要从1(min)->0(min)
        for (int i = pageNumber * itemsPerPage; i < (pageNumber + 1) * itemsPerPage; i++) {
            try {
                record = WelcomeActivity.getDB().findById(Record.class, i);
                if (record != null)
                    records.add(record);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
        return records;
    }

    public boolean delete(int recordid) {
        try {
            WelcomeActivity.getDB().deleteById(Record.class, recordid);
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

//    public int getTotalPageNumber (int itemsPerPage) {
//        if (mRecords == null)
//            return 0;
//
//        int i = mRecords.size() / itemsPerPage;
//        if (mRecords.size() % itemsPerPage > 0) {
//            i ++;
//        }
//        return i;
//    }

    public boolean insert(Person p) {
        boolean ret = false;
        try {
            WelcomeActivity.getDB().saveBindingId(p);
            ret = true;
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG, "WelcomeActivity.getDB().save(mRecord); error!!!");
        } finally {
            Log.i(TAG, "KANGEARALL: " + p.toString());
        }
        return ret;
    }

    public Person queryByFingerId(int fingerId) {
        Person p = null;
        try {
            p = WelcomeActivity.getDB().selector(Person.class).where("fingerId", "=", fingerId).findFirst();
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG, "WelcomeActivity.getDB().save(mRecord); error!!!");
        } finally {
        }
        return p;
    }

    public void check() {
        // check invalid person
//        try {
//            Person p = WelcomeActivity.getDB().selector(Person.class).findFirst();
//            if (p == null) {
//                p = new Person();
//                p.setId(PERSON_ID_INVALID);
//                WelcomeActivity.getDB().saveBindingId(p);
//
//                p = WelcomeActivity.getDB().selector(Person.class).findFirst();
//                Log.i(TAG, "" + p.toString());
//            }
//        } catch (DbException e) {
//            e.printStackTrace();
//        }
    }
}

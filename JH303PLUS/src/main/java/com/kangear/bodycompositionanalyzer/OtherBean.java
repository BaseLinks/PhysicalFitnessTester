package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 18-1-1.
 */

public class OtherBean {
    private static final String TAG = "OtherBean";
    private volatile static OtherBean singleton = null;

    public OtherBean(Context context) {
        init();
    }

    // 请使用Application context
    public static OtherBean getInstance(Context context) {
        if (singleton == null) {
            synchronized (OtherBean.class) {
                if (singleton == null) {
                    singleton = new OtherBean(context.getApplicationContext());
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

    /**
     * @param o
     * @return
     */
    public boolean insert(Other o) {
        boolean ret = false;
        Log.i(TAG, "other: " + o.toString());
        try {
            Other os = WelcomeActivity.getDB().selector(Other.class).where(Other.OTHER_NAME, "=", o.getName()).findFirst();
            if (os != null) {
                WelcomeActivity.getDB().saveOrUpdate(o.setId(os.getId()));
            } else
                WelcomeActivity.getDB().save(o);
            ret = true;
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public Other queryByName(String name) {
        Other p = null;
        try {
            p = WelcomeActivity.getDB().selector(Other.class).where(Other.OTHER_NAME, "=", name).findFirst();
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG, "queryByName error!!!");
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

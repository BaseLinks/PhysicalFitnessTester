package com.kangear.bodycompositionanalyzer;

import android.content.Context;

import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 18-1-1.
 */

public class RecordBean {
    private volatile static RecordBean singleton = null;
    List<Record> mRecords = new ArrayList<>();
    private static final int TEST_RECORD_MAX = 100;
    private Context mContext;

    public RecordBean(Context context) {
        mContext = context;
        init();
    }

    // 请使用Application context
    public static RecordBean getInstance(Context context) {
        if (singleton == null) {
            synchronized (RecordBean.class) {
                if (singleton == null) {
                    singleton = new RecordBean(context);
                }
            }
        }
        return singleton;
    }

    private void init() {
        List<Record> records = mRecords;
        for (int i=0; i<TEST_RECORD_MAX; i++) {
            records.add(new Record(new Person(String.valueOf(i), Person.GENDER_MALE, 26), "2018-01-01"));
        }
    }

//    /**
//     * @param pageNumber 当前页面
//     * @param itemsPerPage  页面长度
//     * @return
//     */
//    public List<Record>  getRecordList(int pageNumber, int itemsPerPage) {
//        List<Record> records = new ArrayList<>();
//        if (pageNumber < 1 || itemsPerPage < 1) {
//            return records;
//        }
//
//        pageNumber --; // 要从1(min)->0(min)
//        for (int i=pageNumber*itemsPerPage; i<(pageNumber + 1)*itemsPerPage; i++) {
//            if (i < mRecords.size())
//                records.add(mRecords.get(i));
//        }
//        return records;
//    }

    /**
     * @param pageNumber 当前页面
     * @param itemsPerPage  页面长度
     * @return
     */
    public List<Record>  getRecordList(int pageNumber, int itemsPerPage) {
        List<Record> records = new ArrayList<>();
        if (pageNumber < 1 || itemsPerPage < 1) {
            return records;
        }

        pageNumber --; // 要从1(min)->0(min)
        for (int i=pageNumber*itemsPerPage; i<(pageNumber + 1)*itemsPerPage; i++) {
            if (i < mRecords.size()) {
                try {
                    records.add(WelcomeActivity.getDB().findById(Record.class, i));
                } catch (DbException e) {
                    e.printStackTrace();
                }
            }
        }
        return records;
    }

    public boolean delete(int recordid) {
        mRecords.remove(recordid);
        return true;
    }

    public int getTotalPageNumber (int itemsPerPage) {
        int i = mRecords.size() / itemsPerPage;
        if (mRecords.size() % itemsPerPage > 0) {
            i ++;
        }
        return i;
    }
}

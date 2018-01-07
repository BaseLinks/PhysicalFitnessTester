package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.util.Log;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 18-1-1.
 */

public class RecordBean {
    private static final String TAG = "RecordBean";
    private volatile static RecordBean singleton = null;
    private List<Record> mRecords = new ArrayList<>();
    private static final int TEST_RECORD_MAX = 100;
    private Context mContext;
    private DbManager mDbManager;

    public RecordBean(Context context) {
        mContext = context;
        mDbManager = WelcomeActivity.getDB();
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

    /**
     * 读取所有数据
     */
    private void init() {
        try {
            mRecords = mDbManager.selector(Record.class).findAll();
            if (mRecords != null)
                Log.i(TAG, "mRecords.size(): " + mRecords.size());
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param pageNumber 当前页面
     * @param itemsPerPage  页面长度
     * @return
     */
    public List<Record>  getRecordList(int pageNumber, int itemsPerPage) {
        List<Record> records = new ArrayList<>();
        Record record;
        if (pageNumber < 0 || itemsPerPage < 1 || mRecords == null || mRecords.size() == 0) {
            return records;
        }
        for (int i = pageNumber * itemsPerPage; i < (pageNumber + 1) * itemsPerPage; i++) {
            if (i < mRecords.size()) {
                record = mRecords.get(i);
                if (record != null) {
                    records.add(mRecords.get(i));
                }
            }
        }
        return records;
    }

    public boolean delete(int recordid) {
        Log.i(TAG, "delete recordid: " + recordid);
        try {
            mDbManager.deleteById(Record.class, recordid);
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
        }
        return true;
    }

    public int getTotalPageNumber (int itemsPerPage) {
        if (mRecords == null)
            return 0;

        int i = mRecords.size() / itemsPerPage;
        if (mRecords.size() % itemsPerPage > 0) {
            i ++;
        }
        return i;
    }

    public boolean insert(Record record) {
        boolean ret = false;
        try {
            Log.i(TAG, "KANGEARALL: " + record.toString());
            mDbManager.saveBindingId(record);
            ret = true;
            init(); // 这里需要date
        } catch (DbException e) {
            e.printStackTrace();
            Log.e(TAG, "mDbManager.save(mRecord); error!!!");
        } finally {
            Log.i(TAG, "KANGEARALL: " + mRecords.toString());
        }
        return ret;
    }

}

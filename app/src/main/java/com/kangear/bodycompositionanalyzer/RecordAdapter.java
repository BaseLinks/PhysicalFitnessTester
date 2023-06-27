package com.kangear.bodycompositionanalyzer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by tony on 18-1-1.
 */

public class RecordAdapter extends BaseAdapter {

    private List<Record> mData;//定义数据。
    private LayoutInflater mInflater;//定义Inflater,加载我们自定义的布局。

    /*
    定义构造器，在Activity创建对象Adapter的时候将数据data和Inflater传入自定义的Adapter中进行处理。
    */
    public RecordAdapter(LayoutInflater inflater,List<Record> data){
        mInflater = inflater;
        mData = data; //testList.sort(Comparator.comparing(ClassName::getFieldName).reversed());;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertview, ViewGroup viewGroup) {
        //获得ListView中的view
        View viewStudent = mInflater.inflate(R.layout.history_banner,null);
        //获得学生对象
        Log.i(TAG, "postition: " + position);
        Record record = mData.get(position);
        if (record != null) {
            //获得自定义布局中每一个控件的对象。
            TextView id = viewStudent.findViewById(R.id.id_textview);
            TextView age = viewStudent.findViewById(R.id.age_textview);
            TextView gender = viewStudent.findViewById(R.id.gender_textview);
            TextView date = viewStudent.findViewById(R.id.date_textview);
            //将数据一一添加到自定义的布局中。
            id.setText(record.getName());
            age.setText(String.valueOf(record.getAge()));
            gender.setText(record.getGender());
            date.setText(getDateFormatted(record.getTime()));
        }
        return viewStudent ;
    }

    public String getDateFormatted(long time){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(time);
    }
}
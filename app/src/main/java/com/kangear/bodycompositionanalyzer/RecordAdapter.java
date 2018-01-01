package com.kangear.bodycompositionanalyzer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

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
        mData = data;
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
        Record record = mData.get(position);
        //获得自定义布局中每一个控件的对象。
        TextView id = (TextView) viewStudent.findViewById(R.id.id_textview);
        TextView age = (TextView) viewStudent.findViewById(R.id.age_textview);
        TextView gender = (TextView) viewStudent.findViewById(R.id.gender_textview);
        TextView date = (TextView) viewStudent.findViewById(R.id.date_textview);
        //将数据一一添加到自定义的布局中。
        id.setText(record.getPerson().getId());
        age.setText(String.valueOf(record.getPerson().getAge()));
        gender.setText(record.getPerson().getGender());
        date.setText(record.getDate());
        return viewStudent ;
    }
}
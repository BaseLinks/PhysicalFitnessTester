package com.kangear.bodycompositionanalyzer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class RecordPdfAdapter extends RecyclerView.Adapter<RecordPdfAdapter.ViewHolder> {
    private String[] mDataset;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mWeightTextView;
        public TextView mGugejiTextView;
        public TextView mTizhibaifenbiTextView;
        public TextView mTestDate;
        public ViewHolder(View v) {
            super(v);
            mWeightTextView = v.findViewById(R.id.weight_textview);
            mGugejiTextView = v.findViewById(R.id.gugeji_textview);
            mTizhibaifenbiTextView = v.findViewById(R.id.tizhibaifenbi_textview);
            mTestDate = v.findViewById(R.id.date_textview);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RecordPdfAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RecordPdfAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pdf_20180115_history, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mWeightTextView.setText(mDataset[position]);
        holder.mGugejiTextView.setText(mDataset[position]);
        holder.mTizhibaifenbiTextView.setText(mDataset[position]);
        holder.mTestDate.setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
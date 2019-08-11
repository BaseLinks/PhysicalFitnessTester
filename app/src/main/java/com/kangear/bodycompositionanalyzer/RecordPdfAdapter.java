package com.kangear.bodycompositionanalyzer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kangear.bodycompositionanalyzer.entry.SchoopiaRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.PdfActivity.DATE_FORMAT;
import static com.kangear.bodycompositionanalyzer.ResultActivity.FLOAT_1_FORMAT;

public class RecordPdfAdapter extends RecyclerView.Adapter<RecordPdfAdapter.ViewHolder> {
    private List<SchoopiaRecord> mDataset;

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
    public RecordPdfAdapter(List<SchoopiaRecord> myDataset) {
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
        SchoopiaRecord record = mDataset.get(position);
        if (record != null) {
//            BodyComposition bc = record.getBodyComposition();
//            if (bc != null) {
                holder.mWeightTextView.setText(record.getRecord().getWeight().get(0) + "kg");
                holder.mGugejiTextView.setText(record.getRecord().getTrunkMuscle().get(0) + "kg");
                holder.mTizhibaifenbiTextView.setText(record.getRecord().getPbf().get(0) + "%");
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
                Date resultdate = record.getCompleteTimeAsLong();
                holder.mTestDate.setText(sdf.format(resultdate));
//            }
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
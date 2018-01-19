package com.kangear.bodycompositionanalyzer;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_PERSON_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * 入口: personId,heck recently 10 times history record by personId
 */
public class PdfActivity extends AppCompatActivity {
    private String TAG = "PdfActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int personId;
    public static final String DATE_FORMAT  = "yy.MM.dd";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_20180115);
        hideSystemUI(getWindow().getDecorView());

        personId = getIntent().getIntExtra(CONST_PERSON_ID, PERSON_ID_INVALID);

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recyclerview);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        Log.i(TAG, "PersonId: " + personId);
        List<Record> mRecords = new ArrayList<>();
        if (personId == PERSON_ID_INVALID) {
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
            mRecords.add(new Record(0, PERSON_ID_INVALID, "default", 25, 180, Person.GENDER_MALE, 70, 0));
        } else {
            mRecords = RecordBean.getInstance(this).getRecordListByPersonId(personId, 0, 10);
            // parse BodyComposition
            for (Record record : mRecords) {
                byte[] data = record.getData();
                if (data != null) {
                    record.setBodyComposition(new BodyComposition(data));
                }
            }
        }
        Log.i(TAG, "mRecords: " + mRecords.size());
        mAdapter = new RecordPdfAdapter(mRecords);
        mRecyclerView.setAdapter(mAdapter);
    }
}

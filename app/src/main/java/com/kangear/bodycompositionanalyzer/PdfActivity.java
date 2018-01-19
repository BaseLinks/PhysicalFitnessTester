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

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.exitAsFail;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.hideSystemUI;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class PdfActivity extends AppCompatActivity {
    private String TAG = "PdfActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pdf_20180115);
        hideSystemUI(getWindow().getDecorView());

        mRecyclerView = (RecyclerView) findViewById(R.id.history_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        List<Record> mRecords = new ArrayList<>();
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
        mAdapter = new RecordPdfAdapter(mRecords);
        mRecyclerView.setAdapter(mAdapter);
    }
}

package com.kangear.bodycompositionanalyzer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.CONST_RECORD_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.INVALID_FINGER_ID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.PERSON_ID_INVALID;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_DELETE;
import static com.kangear.bodycompositionanalyzer.WelcomeActivity.REQUEST_CODE_TOUCHID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class HistoryActivity extends AppCompatActivity {
    private static final String TAG = "HistoryActivity";
    private static final int FIRST_PAGE_NUMBER = 1;
    private static final int LAST_PAGE_NUMBER  = 2;
    private static final int FLESH_PAGE_NUMBER = 3;
    private TextView mPageNumber;
    // TODO: 这里最多只存十个数据
    private List<Record> mData = new ArrayList<>();
    //定义ListView对象
    private ListView mListViewArray;
    private Button mCheckButton;
    private Button mDeleteButton;
    private RecordAdapter mAdapter;
    private static final int PAGE_NUMBER_MIN = 1;
    private static final int COUNTS_PER_PAGE = 10;
    private int mCurPageNumber = PAGE_NUMBER_MIN;
    private int mPosition = PAGE_NUMBER_MIN;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        setContentView(R.layout.activity_history);
        hideSystemUI(getWindow().getDecorView());
        getWindow().setSoftInputMode(WindowManager.
                LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mCheckButton = findViewById(R.id.check_button);
        mDeleteButton= findViewById(R.id.delete_button);
        mPageNumber  = findViewById(R.id.page_number_textview);
        mContext     = this;

        //为ListView对象赋值
        mListViewArray = (ListView) findViewById(R.id.content_listview);
        mListViewArray.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long arg3) {
                Log.i(TAG, "Select: " + position);
                mPosition = position;
                view.setSelected(true);
                mCheckButton.setEnabled(true);
                mDeleteButton.setEnabled(true);
            }
        });

        RecordBean.getInstance(this).setPersonId(PERSON_ID_INVALID);
        page(FLESH_PAGE_NUMBER);
    }

    private void page(int page) {
        int mTotalNumber = RecordBean.getInstance(this).getVipTotalPageNumber(COUNTS_PER_PAGE) + 1;
        mDeleteButton.setEnabled(false);
        mCheckButton.setEnabled(false);

        switch (page) {
            case FIRST_PAGE_NUMBER:
                if (mCurPageNumber > PAGE_NUMBER_MIN)
                    mCurPageNumber --;
                else
                    mCurPageNumber = mTotalNumber;
                break;
            case LAST_PAGE_NUMBER:
                if (mCurPageNumber < mTotalNumber)
                    mCurPageNumber ++;
                else
                    mCurPageNumber = PAGE_NUMBER_MIN;
                break;
            case FLESH_PAGE_NUMBER:
                break;
        }

//        if (mCurPageNumber == 1) {
//            mPreButton.setEnabled(false);
//        } else if (mCurPageNumber == mTotalNumber) {
//            mPreButton.setEnabled(true);
//            mNextButton.setEnabled(false);
//        } else {
//            mPreButton.setEnabled(true);
//            mNextButton.setEnabled(true);
//        }

        mPageNumber.setText(mCurPageNumber + "/" + mTotalNumber);
        LayoutInflater inflater = getLayoutInflater();
        mData = RecordBean.getInstance(this).getVipRecordList(mCurPageNumber - 1, COUNTS_PER_PAGE);
        //创建自定义Adapter的对象
        mAdapter = new RecordAdapter(inflater, mData);
        //将布局添加到ListView中
        mListViewArray.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        // TODO: 如果数据为空，则显示　已经没有更多数据了
        if (mData.size() == 0) {
            Log.e(TAG, "如果数据为空，则显示　已经没有更多数据了");
        } else {
            Log.i(TAG, "" + mData.toString());
        }
    }

    // This snippet hides the system bars.
    public static void hideSystemUI(View v) {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        v.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void onClick(View v) {
        Intent intent = new Intent(this, WelcomeActivity.class);
        switch (v.getId()) {
            case R.id.back_button:
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.next_page_button:
                page(LAST_PAGE_NUMBER);
                break;
            case R.id.previous_page_button:
                page(FIRST_PAGE_NUMBER);
                break;
            case R.id.vip_query_button:
                intent = new Intent(this, TouchIdActivity.class);
                startActivityForResult(intent, REQUEST_CODE_TOUCHID);
                break;
            case R.id.check_button:
                // TODO: 要获取选中jilu_id，给result ui.
                intent = new Intent(this, ResultActivity.class);
                intent.putExtra(CONST_RECORD_ID, mData.get(mPosition).getId());
                startActivity(intent);
                break;
            case R.id.delete_button:
                // TODO: 要获取选中jilu_id，给result ui.
                // 数据库，删除，并刷新当前界面
                startActivityForResult(new Intent(this, HistoryDeleteDialogActivity.class), REQUEST_CODE_DELETE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case REQUEST_CODE_TOUCHID:
                if (resultCode == RESULT_OK) {
                    // get finger id
                    // query all date
                    // TODO: show to user
                    int fingerId = intent.getIntExtra(CONST_FINGER_ID, INVALID_FINGER_ID);
                    if (fingerId != INVALID_FINGER_ID) {
                        Person p = PersonBean.getInstance(this).queryByFingerId(fingerId);
                        if (p != null) {
                            RecordBean.getInstance(this).setPersonId(p.getId());
                            page(PAGE_NUMBER_MIN);
                        }
                    }
                } else {
                    Log.d(TAG, "fuck you");
                }
                break;
            case REQUEST_CODE_DELETE:
                if (resultCode == RESULT_OK) {
                    RecordBean.getInstance(mContext).delete(mData.get(mPosition).getId());
                    page(FLESH_PAGE_NUMBER);
                }
                break;
        }
    }
}

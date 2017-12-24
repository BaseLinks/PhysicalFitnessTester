package com.kangear.bodycompositionanalyzer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_background);
        hideSystemUI(getWindow().getDecorView());
        Log.i(TAG, "onCreate");
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
            case R.id.next_button:
                break;
        }
    }
}

package com.kangear.bodycompositionanalyzer.mvp.presenter;

import android.content.Context;
import android.content.Intent;

import com.kangear.bodycompositionanalyzer.mvp.ui.activity.FdActivity;

public class AuthPresenter {

    public static void startAuth(Context context) {
        context.startActivity(new Intent(context, FdActivity.class));
    }
}

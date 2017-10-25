package com.example.seokchankwon.floationactiondialog.dialog;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;
import android.view.Window;

/**
 * Created by seokchan.kwon on 2017. 10. 20..
 */

public class BaseAppcompatDialog extends AppCompatDialog {

    public BaseAppcompatDialog(Context context) {
        this(context, 0);
    }

    public BaseAppcompatDialog(Context context, int theme) {
        super(context, theme);
        init();
    }

    protected BaseAppcompatDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init();
    }

    private void init() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

}

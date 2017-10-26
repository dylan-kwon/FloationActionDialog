package com.example.seokchankwon.floationactiondialog.dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by chan on 2017. 3. 30..
 */

public abstract class BaseDialogFragment extends AppCompatDialogFragment {

    @IntDef({Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM,
            Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL})
    private @interface dialogGravity {
        // Dialog Gravity
    }

    private OnDismissListener mOnDismissListener;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getWindow();

        if (window != null) {

            WindowManager.LayoutParams lpWindow = window.getAttributes();
            lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.dimAmount = 0.8f;

            window.setAttributes(lpWindow);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Nullable
    public Window getWindow() {
        return getDialog().getWindow();
    }

    public WindowManager.LayoutParams getLayoutParams() {
        Window window = getWindow();
        if (window != null) {
            return window.getAttributes();
        }
        return null;
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        if (isAdded()) {
            super.onDismiss(dialog);
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!isAdded()) {
            super.show(manager, tag);
        }
    }

    public void showAllowingStateLoss(FragmentManager fm, String tag) {
        if (!isAdded()) {
            fm.beginTransaction()
                    .add(this, tag)
                    .commitAllowingStateLoss();
        }
    }

    @Override
    public void dismiss() {
        if (isAdded()) {
            super.dismiss();
        }
    }

}

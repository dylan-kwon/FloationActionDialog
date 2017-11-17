package com.example.seokchankwon.floationactiondialog.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
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

    private final String TAG = this.getClass().getSimpleName();

    public static final String EXTRA_GRAVITY = "extra.gravity";
    public static final String EXTRA_DIM_AMOUNT = "extra.dim_amount";

    public static final float DEFAULT_DIM_AMOUNT = 0.8f;

    @IntDef({Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL})
    private @interface DialogGravity {
        // Dialog Gravity
    }

    private int mGravity;
    private float mDimAmount;

    private OnDismissListener mOnDismissListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupDialogState(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Window window = getWindow();

        if (window != null) {

            WindowManager.LayoutParams lpWindow = window.getAttributes();

            lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
            lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lpWindow.gravity = mGravity;
            lpWindow.dimAmount = mDimAmount;

            window.setAttributes(lpWindow);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 기본적으로 onPause 이후 호출
        outState.putInt(EXTRA_GRAVITY, getGravity());
        outState.putFloat(EXTRA_DIM_AMOUNT, getDimAmount());
    }

    private void setupDialogState(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mGravity = savedInstanceState.getInt(EXTRA_GRAVITY, Gravity.CENTER);
            mDimAmount = savedInstanceState.getFloat(EXTRA_DIM_AMOUNT, DEFAULT_DIM_AMOUNT);

        } else {
            if (mGravity == 0) {
                mGravity = Gravity.CENTER;
            }

            if (mDimAmount == 0) {
                mDimAmount = DEFAULT_DIM_AMOUNT;
            }
        }
    }

    @Nullable
    public Window getWindow() {
        Dialog dialog = getDialog();
        if (dialog != null) {
            return getDialog().getWindow();
        }
        return null;
    }

    @Nullable
    public WindowManager.LayoutParams getLayoutParams() {
        Window window = getWindow();
        if (window != null) {
            return window.getAttributes();
        }
        return null;
    }

    public void setLayoutParams(@NonNull WindowManager.LayoutParams layoutParams) {
        Window window = getWindow();
        if (window != null) {
            window.setAttributes(layoutParams);
        }
    }

    @DialogGravity
    public int getGravity() {
        WindowManager.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            return lp.gravity;
        }
        return mGravity;
    }

    public void setGravity(@DialogGravity int gravity) {
        WindowManager.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            lp.gravity = gravity;
            setLayoutParams(lp);
        }
        mGravity = gravity;
    }

    public float getDimAmount() {
        WindowManager.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            return lp.dimAmount;
        }
        return mDimAmount;
    }

    public void setDimAmount(float dimAmount) {
        WindowManager.LayoutParams lp = getLayoutParams();
        if (lp != null) {
            lp.dimAmount = mDimAmount;
            setLayoutParams(lp);
        }
        mDimAmount = dimAmount;
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

    public void finish() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    public void setOnDismissListener(@Nullable OnDismissListener listener) {
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

    protected static abstract class Builder<T extends Builder, S extends BaseDialogFragment> {

        private int mGravity;
        private float mDimAmount;

        private boolean mCancelable;

        private OnDismissListener mOnDismissListener;

        public Builder() {
            mGravity = Gravity.CENTER;
            mDimAmount = DEFAULT_DIM_AMOUNT;
            mCancelable = true;
            mOnDismissListener = null;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public T setGravity(@DialogGravity int gravity) {
            mGravity = gravity;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public T setDimAmount(float dimAmount) {
            mDimAmount = dimAmount;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public T setCancelable(boolean cancelable) {
            mCancelable = cancelable;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        public T setOnDismissListener(@Nullable OnDismissListener listener) {
            mOnDismissListener = listener;
            return (T) this;
        }

        protected void setDialogState(@NonNull S dialog) {
            dialog.setGravity(mGravity);
            dialog.setDimAmount(mDimAmount);
            dialog.setCancelable(mCancelable);
            dialog.setOnDismissListener(mOnDismissListener);
        }

        @NonNull
        public abstract S build();

    }

}

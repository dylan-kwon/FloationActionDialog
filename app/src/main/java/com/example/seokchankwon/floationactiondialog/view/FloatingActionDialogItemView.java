package com.example.seokchankwon.floationactiondialog.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Px;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.seokchankwon.floationactiondialog.R;

/**
 * Created by seokchan.kwon on 2017. 10. 25..
 */

public class FloatingActionDialogItemView extends FrameLayout {

    private FloatingActionButton fabItem;

    private TextView tvItemLabel;


    public FloatingActionDialogItemView(@NonNull Context context) {
        this(context, null);
    }

    public FloatingActionDialogItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatingActionDialogItemView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    private void initView(@Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_floating_action_dialog_item, this, false);
        addView(view);

        fabItem = view.findViewById(R.id.fab_view_floating_action_dialog_item);
        tvItemLabel = view.findViewById(R.id.tv_view_floating_action_dialog_item_label);

        setViewData(attrs, defStyleAttr);
    }

    private void setViewData(@Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FloatingActionDialogItemView, defStyleAttr, 0);

        int fabSrc = typedArray.getResourceId(R.styleable.FloatingActionDialogItemView_fabSrc, 0);
        int fabBackground = typedArray.getColor(R.styleable.FloatingActionDialogItemView_fabBackground, ContextCompat.getColor(getContext(), R.color.colorAccent));

        setFabImageResource(fabSrc);
        setFabBackgroundColor(fabBackground);

        String labelText = typedArray.getString(R.styleable.FloatingActionDialogItemView_label);
        float labelTextSize = typedArray.getDimension(R.styleable.FloatingActionDialogItemView_labelTextSize, getResources().getDimensionPixelSize(R.dimen.dim_14dp));
        int labelTextColor = typedArray.getColor(R.styleable.FloatingActionDialogItemView_labelTextColor, ContextCompat.getColor(getContext(), R.color.color_ffffff));

        setLabelText(labelText);
        setLabelTextSize(labelTextSize);
        setLabelTextColor(labelTextColor);

        typedArray.recycle();
    }

    public FloatingActionButton getFab() {
        return fabItem;
    }

    public TextView getTvLabel() {
        return tvItemLabel;
    }

    @Nullable
    public Drawable getFabSrc() {
        return fabItem.getDrawable();
    }

    public void setFabImageResource(@DrawableRes int imageResource) {
        fabItem.setImageResource(imageResource);
    }

    @Nullable
    public ColorStateList getFabBackgroundColor() {
        return fabItem.getBackgroundTintList();
    }

    public void setFabBackgroundColor(int color) {
        fabItem.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    @NonNull
    public String getLabelText() {
        String labelText = tvItemLabel.getText().toString();
        if (TextUtils.isEmpty(labelText)) {
            return "";
        }
        return labelText;
    }

    public void setLabelText(@Nullable String text) {
        if (TextUtils.isEmpty(text)) {
            tvItemLabel.setText("");
        } else {
            tvItemLabel.setText(text);
        }
    }

    @Px
    public int getLabelTextSize() {
        return (int) tvItemLabel.getTextSize();
    }

    public void setLabelTextSize(float textSize) {
        tvItemLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    @Nullable
    public ColorStateList getLabelTextColor() {
        return tvItemLabel.getTextColors();
    }

    public void setLabelTextColor(int color) {
        tvItemLabel.setTextColor(color);
    }

}

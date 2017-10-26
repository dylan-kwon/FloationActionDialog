package com.example.seokchankwon.floationactiondialog.dialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import com.example.seokchankwon.floationactiondialog.R;
import com.example.seokchankwon.floationactiondialog.listener.SimpleAnimatorListener;
import com.example.seokchankwon.floationactiondialog.view.FloatingActionDialogItemView;

import java.util.ArrayList;

/**
 * Created by seokchan.kwon on 2017. 10. 24..
 */

public class FloatingActionDialog extends BaseDialogFragment {

    public static final String EXTRA_LOCATION_X = "extra.location_x";
    public static final String EXTRA_LOCATION_Y = "extra.location_y";
    public static final String EXTRA_ITEMS = "extra.items";
    public static final String EXTRA_ITEM_BACKGROUND_COLOR = "extra.item_background_color";
    public static final String EXTRA_CLOSER_BACKGROUND_COLOR = "extra.closer_background_color";
    public static final String EXTRA_LABEL_ANIMATION_DURATION = "extra.label_animation_duration";
    public static final String EXTRA_SHOW_ANIMATION_DURATION = "extra.show_animation_duration";
    public static final String EXTRA_DISMISS_ANIMATION_DURATION = "extra.dismiss_animation_duration";

    private int mLocationX;
    private int mLocationY;
    private int mItemBackgroundColorId;
    private int mCloserBackgroundColorId;
    private int mShowAnimationDuration;
    private int mDismissAnimationDuration;
    private int mLabelAnimationDuration;

    private ArrayList<Item> mItems;
    private ArrayList<FloatingActionDialogItemView> mItemViews;

    private OnItemClickListener mOnItemClickListener;

    private Context mContext;

    private ConstraintLayout clBackground;

    private FrameLayout flItemContainer;

    private FloatingActionButton fabClose;


    public FloatingActionDialog() {
        // newInstance를 사용
    }

    private static FloatingActionDialog newInstance() {
        FloatingActionDialog dialog = new FloatingActionDialog();
        Bundle bundle = new Bundle();
        dialog.setArguments(bundle);
        return dialog;
    }

    public static FloatingActionDialog newInstance(@NonNull Builder builder) {
        FloatingActionDialog dialog = newInstance();
        dialog.getArguments().putInt(EXTRA_LOCATION_X, builder.mLocationX);
        dialog.getArguments().putInt(EXTRA_LOCATION_Y, builder.mLocationY);
        dialog.getArguments().putInt(EXTRA_ITEM_BACKGROUND_COLOR, builder.mItemBackgroundColorId);
        dialog.getArguments().putInt(EXTRA_CLOSER_BACKGROUND_COLOR, builder.mCloserBackgroundColorId);
        dialog.getArguments().putInt(EXTRA_SHOW_ANIMATION_DURATION, builder.mShowAnimationDuration);
        dialog.getArguments().putInt(EXTRA_DISMISS_ANIMATION_DURATION, builder.mDismissAnimationDuration);
        dialog.getArguments().putInt(EXTRA_LABEL_ANIMATION_DURATION, builder.mLabelAnimationDuration);
        dialog.getArguments().putParcelableArrayList(EXTRA_ITEMS, builder.mItems);
        return dialog;
    }

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mItemViews = new ArrayList<>();
        setupInstanceState(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BaseAppcompatDialog(getContext(), getTheme()) {
            @Override
            public void onBackPressed() {
                dismissAnimation(new OnAnimationEndCallback() {
                    @Override
                    public void onAnimationEnd() {
                        dismiss();
                    }
                });
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_floating_action, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView(getView());

        fabClose.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                fabClose.getViewTreeObserver().removeOnPreDrawListener(this);

                fabClose.setX(mLocationX);
                fabClose.setY(mLocationY);

                fabClose.setBackgroundTintList(ColorStateList.valueOf(
                        ContextCompat.getColor(mContext, mCloserBackgroundColorId)));

                flItemContainer.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        flItemContainer.getViewTreeObserver().removeOnPreDrawListener(this);

                        makeItemView(mItems);
                        showAnimation();

                        return false;
                    }
                });

                return false;
            }
        });

        fabClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAnimation(new OnAnimationEndCallback() {
                    @Override
                    public void onAnimationEnd() {
                        dismiss();
                    }
                });
                fabClose.setEnabled(false);
            }
        });

        clBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAnimation(new OnAnimationEndCallback() {
                    @Override
                    public void onAnimationEnd() {
                        dismiss();
                    }
                });
                clBackground.setEnabled(false);
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_LOCATION_X, mLocationX);
        outState.putInt(EXTRA_LOCATION_Y, mLocationY);
        outState.putInt(EXTRA_ITEM_BACKGROUND_COLOR, mItemBackgroundColorId);
        outState.putInt(EXTRA_CLOSER_BACKGROUND_COLOR, mCloserBackgroundColorId);
        outState.putInt(EXTRA_SHOW_ANIMATION_DURATION, mShowAnimationDuration);
        outState.putInt(EXTRA_DISMISS_ANIMATION_DURATION, mDismissAnimationDuration);
        outState.putInt(EXTRA_LABEL_ANIMATION_DURATION, mLabelAnimationDuration);
        outState.putParcelableArrayList(EXTRA_SHOW_ANIMATION_DURATION, mItems);
    }

    private void setupInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mLocationX = savedInstanceState.getInt(EXTRA_LOCATION_X);
            mLocationY = savedInstanceState.getInt(EXTRA_LOCATION_Y);
            mItemBackgroundColorId = savedInstanceState.getInt(EXTRA_ITEM_BACKGROUND_COLOR);
            mCloserBackgroundColorId = savedInstanceState.getInt(EXTRA_CLOSER_BACKGROUND_COLOR);
            mShowAnimationDuration = savedInstanceState.getInt(EXTRA_SHOW_ANIMATION_DURATION);
            mDismissAnimationDuration = savedInstanceState.getInt(EXTRA_DISMISS_ANIMATION_DURATION);
            mLabelAnimationDuration = savedInstanceState.getInt(EXTRA_LABEL_ANIMATION_DURATION);
            mItems = savedInstanceState.getParcelableArrayList(EXTRA_ITEMS);

        } else {
            mLocationX = getArguments().getInt(EXTRA_LOCATION_X);
            mLocationY = getArguments().getInt(EXTRA_LOCATION_Y);
            mItemBackgroundColorId = getArguments().getInt(EXTRA_ITEM_BACKGROUND_COLOR);
            mCloserBackgroundColorId = getArguments().getInt(EXTRA_CLOSER_BACKGROUND_COLOR);
            mShowAnimationDuration = getArguments().getInt(EXTRA_SHOW_ANIMATION_DURATION);
            mDismissAnimationDuration = getArguments().getInt(EXTRA_DISMISS_ANIMATION_DURATION);
            mLabelAnimationDuration = getArguments().getInt(EXTRA_LABEL_ANIMATION_DURATION);
            mItems = getArguments().getParcelableArrayList(EXTRA_ITEMS);
        }

        if (mItems == null) {
            mItems = new ArrayList<>();
        }
    }

    private void initView(View view) {
        clBackground = view.findViewById(R.id.cl_dialog_floating_action_background);
        flItemContainer = view.findViewById(R.id.fl_dialog_floating_action_item_container);
        fabClose = view.findViewById(R.id.fab_dialog_floating_action_close);
    }

    public int getItemCount() {
        return mItems.size();
    }

    public int getItemViewCount() {
        return mItemViews.size();
    }

    private void makeItemView(@Nullable ArrayList<Item> items) {
        if (items == null) {
            return;
        }

        final int size = getItemCount();

        for (int i = 0; i < size; i++) {
            final int itemPosition = i;
            final Item item = mItems.get(i);

            final FloatingActionDialogItemView itemView = new FloatingActionDialogItemView(mContext);

            itemView.setLabelText(item.getLabel());
            itemView.setFabImageResource(item.getImageRes());
            itemView.setFabBackgroundColor(ContextCompat.getColor(mContext, mItemBackgroundColorId));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(itemPosition, item);
                    }
                }
            });

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.RIGHT);

            flItemContainer.addView(itemView, lp);
            mItemViews.add(itemView);

            if (i != size - 1) {
                continue;
            }

            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    itemView.getViewTreeObserver().removeOnPreDrawListener(this);

                    ViewGroup.LayoutParams lp = flItemContainer.getLayoutParams();
                    lp.height = itemView.getHeight() * (size + 1);
                    flItemContainer.setLayoutParams(lp);

                    int x = mLocationX - flItemContainer.getWidth() + fabClose.getWidth();
                    int y = (int) (mLocationY - lp.height + fabClose.getHeight() - getResources().getDimension(R.dimen.dim_10dp));

                    flItemContainer.setX(x);
                    flItemContainer.setY(y);

                    return false;
                }
            });
        }
    }

    @Nullable
    public FloatingActionDialogItemView getItemView(int itemPosition) {
        return mItemViews.get(itemPosition);
    }

    @Nullable
    public ArrayList<FloatingActionDialogItemView> getItemViews() {
        return mItemViews;
    }

    private void showAnimation() {
        // 닫기 버튼 왼쪽으로 45도 회전 (x 모양이 되도록 회전)
        fabClose.animate()
                .rotation(-45)
                .setDuration(mShowAnimationDuration)
                .start();

        final int itemCount = getItemViewCount();
        final int[] translationY = {fabClose.getHeight()};

        // 아이템의 개수만큼 반복
        for (int i = 0; i < itemCount; i++) {
            final FloatingActionDialogItemView itemView = getItemView(i);

            if (itemView == null) {
                return;
            }

            // 뷰가 그려질 때까지 기다림
            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    itemView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // 아이템뷰를 간격에 맞게 위로 이동 애니메이션 시작
                    itemViewTranslateYAnimation(itemView, translationY[0], new OnAnimationEndCallback() {
                        @Override
                        public void onAnimationEnd() {
                            // 아이템뷰 이동 애니메이션이 끝나면 라벨 나타나기 애니메이션 시작
                            labelAlphaAnimation(itemView, 1.0f, null);
                        }
                    });
                    translationY[0] += itemView.getHeight();
                    return false;
                }
            });
        }
    }

    private void dismissAnimation(final OnAnimationEndCallback callback) {
        // 닫기 버튼 원위치 (+모양이 되도록 회전)
        fabClose.animate()
                .rotation(0)
                .setDuration(mDismissAnimationDuration)
                .start();

        final int itemCount = getItemViewCount();

        // 아이템의 개수만큼 반복
        for (int i = itemCount - 1; i >= 0; i--) {
            final int tempI = i;
            final FloatingActionDialogItemView itemView = getItemView(i);

            if (itemView == null) {
                return;
            }

            // 뷰가 그려질 때까지 기다림
            itemView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    itemView.getViewTreeObserver().removeOnPreDrawListener(this);

                    // 라벨 감추기 애니메이션 시작
                    labelAlphaAnimation(itemView, 0.0f, new OnAnimationEndCallback() {
                        @Override
                        public void onAnimationEnd() {
                            // 라벨 감추기 애니메이션이 끝나면 아이템뷰 원위치 애니메이션 시작
                            itemViewTranslateYAnimation(itemView, 0, new OnAnimationEndCallback() {
                                @Override
                                public void onAnimationEnd() {
                                    // 처음의 (반복문 마지막) 아이템뷰 원위치 애니메이션이 끝나면 콜백 호출
                                    if (tempI == 0) {
                                        mHandler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                callback.onAnimationEnd();
                                            }
                                        }, mDismissAnimationDuration);
                                    }
                                }
                            });
                        }
                    });
                    return false;
                }
            });
        }
    }

    private void itemViewTranslateYAnimation(@NonNull final FloatingActionDialogItemView itemView, final int translationY, @Nullable final OnAnimationEndCallback callback) {
        Log.e("test", "test1");
        ViewPropertyAnimator animator = itemView.animate();

        animator.translationY(-translationY);
        animator.setDuration(mShowAnimationDuration);

        if (translationY > 0) {
            animator.setInterpolator(new OvershootInterpolator());
        } else {
            animator.setInterpolator(new AnticipateInterpolator());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            animator.withLayer();
        }

        animator.setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                itemView.animate().setListener(null);

                if (callback != null) {
                    callback.onAnimationEnd();
                }
            }
        });

        animator.start();
    }

    private void labelAlphaAnimation(@NonNull final FloatingActionDialogItemView itemView, float alpha, @Nullable final OnAnimationEndCallback callback) {
        Log.e("test", "test2");
        itemView.getTvLabel().animate()
                .alpha(alpha)
                .setDuration(mLabelAnimationDuration)
                .setListener(new SimpleAnimatorListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        itemView.getTvLabel().animate().setListener(null);

                        if (callback != null) {
                            callback.onAnimationEnd();
                        }
                    }
                })
                .start();
    }

    Handler mHandler = new Handler();

    public void setOnItemClickListener(@Nullable OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public static class Builder {

        private int MAXIMUM_ITEM_COUNT = 5;

        private int mLocationX;
        private int mLocationY;
        private int mItemBackgroundColorId;
        private int mCloserBackgroundColorId;
        private int mShowAnimationDuration;
        private int mDismissAnimationDuration;
        private int mLabelAnimationDuration;

        private ArrayList<Item> mItems;

        private OnDismissListener mOnDismissListener;
        private OnItemClickListener mOnItemClickListener;

        public Builder(View anchorView) {
            int[] locations = new int[2];
            anchorView.getLocationOnScreen(locations);

            mLocationX = locations[0];
            mLocationY = locations[1] - getStatusBarSize(anchorView.getContext());

            mItemBackgroundColorId = R.color.colorAccent;
            mCloserBackgroundColorId = R.color.colorPrimary;

            mShowAnimationDuration = 300;
            mDismissAnimationDuration = 200;
            mLabelAnimationDuration = 150;

            mItems = new ArrayList<>();
        }

        public Builder setShowAnimationDuration(int duration) {
            mShowAnimationDuration = duration;
            return this;
        }

        public Builder setDismissAnimationDuration(int duration) {
            mDismissAnimationDuration = duration;
            return this;
        }

        public Builder setLabelAnimationDuration(int duration) {
            mLabelAnimationDuration = duration;
            return this;
        }

        public Builder addItem(@Nullable Item item) {
            if (item == null) {
                return this;
            }
            if (mItems.size() >= MAXIMUM_ITEM_COUNT) {
                throw new IndexOutOfBoundsException("FloatingActionDialog의 아이템은 최대 5개까지 가능합니다!!");
            }
            mItems.add(item);
            return this;
        }

        public Builder addItems(@Nullable ArrayList<Item> items) {
            if (items == null) {
                return this;
            }
            if (items.size() > MAXIMUM_ITEM_COUNT) {
                throw new IndexOutOfBoundsException("FloatingActionDialog의 아이템은 최대 5개까지 가능합니다!!");
            }
            mItems.addAll(items);
            return this;
        }

        public Builder setItemBackgroundColor(@ColorRes int id) {
            mItemBackgroundColorId = id;
            return this;
        }

        public Builder setCloserBackgroundColor(@ColorRes int id) {
            mCloserBackgroundColorId = id;
            return this;
        }

        public Builder setOnDismissListener(@Nullable OnDismissListener listener) {
            mOnDismissListener = listener;
            return this;
        }

        public Builder setOnItemClickListener(@Nullable OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        public FloatingActionDialog build() {
            FloatingActionDialog dialog = FloatingActionDialog.newInstance(this);
            dialog.setOnDismissListener(mOnDismissListener);
            dialog.setOnItemClickListener(mOnItemClickListener);
            return dialog;
        }

    }

    public static class Item implements Parcelable {

        @DrawableRes
        public int mImageRes;

        public String mLabel;

        public Item(@DrawableRes int imageRes, @Nullable String label) {
            mImageRes = imageRes;
            mLabel = label;
        }

        protected Item(Parcel in) {
            mImageRes = in.readInt();
            mLabel = in.readString();
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(mImageRes);
            dest.writeString(mLabel);
        }

        @DrawableRes
        public int getImageRes() {
            return mImageRes;
        }

        @Nullable
        public String getLabel() {
            return mLabel;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Item item);
    }

    public interface OnAnimationEndCallback {
        void onAnimationEnd();
    }

    public static int getStatusBarSize(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}

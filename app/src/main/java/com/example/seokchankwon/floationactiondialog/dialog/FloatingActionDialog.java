package com.example.seokchankwon.floationactiondialog.dialog;

import android.animation.Animator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.PopupMenu;

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
    public static final String EXTRA_MENU_ID = "extra.menu_id";
    public static final String EXTRA_ITEM_BACKGROUND_COLOR = "extra.item_background_color";
    public static final String EXTRA_CLOSER_BACKGROUND_COLOR = "extra.closer_background_color";
    public static final String EXTRA_LABEL_ANIMATION_DURATION = "extra.label_animation_duration";
    public static final String EXTRA_SHOW_ANIMATION_DURATION = "extra.show_animation_duration";
    public static final String EXTRA_DISMISS_ANIMATION_DURATION = "extra.dismiss_animation_duration";

    private int mMenuId;
    private int mLocationX;
    private int mLocationY;
    private int mItemBackgroundColorId;
    private int mCloserBackgroundColorId;
    private int mShowAnimationDuration;
    private int mDismissAnimationDuration;
    private int mLabelAnimationDuration;

    private Menu mMenu;

    private ArrayList<FloatingActionDialogItemView> mItemViews;

    private Context mContext;

    private ConstraintLayout clBackground;

    private FrameLayout flItemContainer;

    private FloatingActionButton fabClose;

    private OnItemClickListener mOnItemClickListener;


    public FloatingActionDialog() {
        // newInstance를 사용
    }

    private static FloatingActionDialog newInstance(@NonNull Builder builder) {
        FloatingActionDialog dialog = new FloatingActionDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_MENU_ID, builder.mMenuId);
        bundle.putInt(EXTRA_LOCATION_X, builder.mLocationX);
        bundle.putInt(EXTRA_LOCATION_Y, builder.mLocationY);
        bundle.putInt(EXTRA_ITEM_BACKGROUND_COLOR, builder.mItemBackgroundColorId);
        bundle.putInt(EXTRA_CLOSER_BACKGROUND_COLOR, builder.mCloserBackgroundColorId);
        bundle.putInt(EXTRA_SHOW_ANIMATION_DURATION, builder.mShowAnimationDuration);
        bundle.putInt(EXTRA_DISMISS_ANIMATION_DURATION, builder.mDismissAnimationDuration);
        bundle.putInt(EXTRA_LABEL_ANIMATION_DURATION, builder.mLabelAnimationDuration);
        dialog.setArguments(bundle);
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_floating_action, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();

        if (activity == null) {
            return;
        }

        PopupMenu popupMenu = new PopupMenu(mContext, null);
        mMenu = popupMenu.getMenu();

        activity.getMenuInflater().inflate(mMenuId, mMenu);

        if (mMenu.size() > 5) {
            throw new IndexOutOfBoundsException("menuItem의 개수는 최대 5개 까지만 가능하다.");
        }

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

                        makeItemView();
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
        outState.putInt(EXTRA_MENU_ID, mMenuId);
        outState.putInt(EXTRA_LOCATION_X, mLocationX);
        outState.putInt(EXTRA_LOCATION_Y, mLocationY);
        outState.putInt(EXTRA_ITEM_BACKGROUND_COLOR, mItemBackgroundColorId);
        outState.putInt(EXTRA_CLOSER_BACKGROUND_COLOR, mCloserBackgroundColorId);
        outState.putInt(EXTRA_SHOW_ANIMATION_DURATION, mShowAnimationDuration);
        outState.putInt(EXTRA_DISMISS_ANIMATION_DURATION, mDismissAnimationDuration);
        outState.putInt(EXTRA_LABEL_ANIMATION_DURATION, mLabelAnimationDuration);
    }

    private void setupInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mMenuId = savedInstanceState.getInt(EXTRA_MENU_ID);
            mLocationX = savedInstanceState.getInt(EXTRA_LOCATION_X);
            mLocationY = savedInstanceState.getInt(EXTRA_LOCATION_Y);
            mItemBackgroundColorId = savedInstanceState.getInt(EXTRA_ITEM_BACKGROUND_COLOR);
            mCloserBackgroundColorId = savedInstanceState.getInt(EXTRA_CLOSER_BACKGROUND_COLOR);
            mShowAnimationDuration = savedInstanceState.getInt(EXTRA_SHOW_ANIMATION_DURATION);
            mDismissAnimationDuration = savedInstanceState.getInt(EXTRA_DISMISS_ANIMATION_DURATION);
            mLabelAnimationDuration = savedInstanceState.getInt(EXTRA_LABEL_ANIMATION_DURATION);

        } else if (getArguments() != null) {
            mMenuId = getArguments().getInt(EXTRA_MENU_ID);
            mLocationX = getArguments().getInt(EXTRA_LOCATION_X);
            mLocationY = getArguments().getInt(EXTRA_LOCATION_Y);
            mItemBackgroundColorId = getArguments().getInt(EXTRA_ITEM_BACKGROUND_COLOR);
            mCloserBackgroundColorId = getArguments().getInt(EXTRA_CLOSER_BACKGROUND_COLOR);
            mShowAnimationDuration = getArguments().getInt(EXTRA_SHOW_ANIMATION_DURATION);
            mDismissAnimationDuration = getArguments().getInt(EXTRA_DISMISS_ANIMATION_DURATION);
            mLabelAnimationDuration = getArguments().getInt(EXTRA_LABEL_ANIMATION_DURATION);
        }
    }

    private void initView(View view) {
        clBackground = view.findViewById(R.id.cl_dialog_floating_action_background);
        flItemContainer = view.findViewById(R.id.fl_dialog_floating_action_item_container);
        fabClose = view.findViewById(R.id.fab_dialog_floating_action_close);
    }

    @NonNull
    public Menu getMenu() {
        return mMenu;
    }

    @NonNull
    public MenuItem getMenuItem(int index) {
        return mMenu.getItem(index);
    }

    public int getMenuSize() {
        return mMenu.size();
    }

    @NonNull
    private ArrayList<FloatingActionDialogItemView> getItemViews() {
        return mItemViews;
    }

    @NonNull
    private FloatingActionDialogItemView getItemView(int itemPosition) {
        return mItemViews.get(itemPosition);
    }

    private void makeItemView() {
        final int size = getMenuSize();

        for (int i = (size - 1); i >= 0; i--) {
            final MenuItem menuItem = getMenuItem(i);

            final FloatingActionDialogItemView itemView = new FloatingActionDialogItemView(mContext);

            String itemTitle = String.valueOf(menuItem.getTitle());
            Drawable itemIcon = menuItem.getIcon();

            itemView.setLabelText(itemTitle);
            itemView.setFabImageResource(itemIcon);
            itemView.setFabBackgroundColor(ContextCompat.getColor(mContext, mItemBackgroundColorId));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAnimation(new OnAnimationEndCallback() {
                        @Override
                        public void onAnimationEnd() {
                            if (mOnItemClickListener != null) {
                                mOnItemClickListener.onItemClick(menuItem);
                            }
                            dismiss();
                        }
                    });
                }
            });

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM | Gravity.RIGHT);

            flItemContainer.addView(itemView, lp);
            mItemViews.add(itemView);

            if (i != 0) {
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

    private void showAnimation() {
        // 닫기 버튼 왼쪽으로 45도 회전 (x 모양이 되도록 회전)
        fabClose.animate()
                .rotation(-45)
                .setDuration(mShowAnimationDuration)
                .start();

        final int itemCount = getMenuSize();
        final int[] translationY = {fabClose.getHeight()};

        // 아이템의 개수만큼 반복
        for (int i = 0; i < itemCount; i++) {
            final FloatingActionDialogItemView itemView = getItemView(i);

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

        final int itemCount = getMenuSize();

        // 아이템의 개수만큼 반복
        for (int i = itemCount - 1; i >= 0; i--) {
            final int tempI = i;
            final FloatingActionDialogItemView itemView = getItemView(i);

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

    public static class Builder extends BaseDialogFragment.Builder<Builder, FloatingActionDialog> {

        private int mLocationX;
        private int mLocationY;
        private int mItemBackgroundColorId;
        private int mCloserBackgroundColorId;
        private int mShowAnimationDuration;
        private int mDismissAnimationDuration;
        private int mLabelAnimationDuration;

        private int mMenuId;

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
        }

        public Builder setMenu(@MenuRes int menuId) {
            mMenuId = menuId;
            return this;
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

        public Builder setItemBackgroundColor(@ColorRes int resId) {
            mItemBackgroundColorId = resId;
            return this;
        }

        public Builder setCloserBackgroundColor(@ColorRes int resId) {
            mCloserBackgroundColorId = resId;
            return this;
        }

        public Builder setOnItemClickListener(@Nullable OnItemClickListener listener) {
            mOnItemClickListener = listener;
            return this;
        }

        @NonNull
        @Override
        public FloatingActionDialog build() {
            FloatingActionDialog dialog = FloatingActionDialog.newInstance(this);
            dialog.setOnItemClickListener(mOnItemClickListener);
            setDialogState(dialog);
            return dialog;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(@NonNull MenuItem menuItem);
    }

    public interface OnAnimationEndCallback {
        void onAnimationEnd();
    }

    public static int getStatusBarSize(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }
}

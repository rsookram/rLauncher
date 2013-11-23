package com.merono.rlauncher;

import android.view.View;

public class TranslateViewDrawerListener extends FixedOpenDrawerLayout.SimpleDrawerListener {

    private final View mTranslateView;
    private final int mTranslateOffset;

    public TranslateViewDrawerListener(View translateView, int translationOffset) {
        mTranslateView = translateView;
        mTranslateOffset = translationOffset;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (mTranslateView != null) {
            mTranslateView.animate()
            .translationX(slideOffset * mTranslateOffset)
            .setDuration(0L)
            .start();
        }
    }
}

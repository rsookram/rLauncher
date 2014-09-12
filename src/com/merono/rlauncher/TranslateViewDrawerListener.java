package com.merono.rlauncher;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

public class TranslateViewDrawerListener extends DrawerLayout.SimpleDrawerListener {

    private final ViewGroup mFab;
    private final int mFabTopOffset;

    private final ViewGroup mTranslateView;
    private final int mTranslateOffset;

    public TranslateViewDrawerListener(ViewGroup fab, ViewGroup translateView, int translationOffset) {
        mFab = fab;
        mFabTopOffset = fab.getContext().getResources().getDimensionPixelSize(R.dimen.side_margin);

        mTranslateView = translateView;
        mTranslateOffset = translationOffset;
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);

        mFab.setY(((mFab.getTop() - mFabTopOffset) * (1.0F - slideOffset)) + mFabTopOffset);

        float shiftedX = (slideOffset - 0.5F);
        float fabAlpha = 4.0F * shiftedX * shiftedX;
        mFab.setAlpha(fabAlpha);

        mFab.getChildAt(0).setAlpha(1.0F - slideOffset);
        mFab.getChildAt(1).setAlpha(slideOffset);

        mTranslateView.setX(slideOffset * mTranslateOffset);
        mTranslateView.setAlpha((1.0F - (slideOffset * 0.5F)));

        for (int i = 0; i < mTranslateView.getChildCount(); i++) {
            View v = mTranslateView.getChildAt(i);
            v.setScaleX(1.0F - (slideOffset * 0.5F));
            v.setScaleY(1.0F - (slideOffset * 0.5F));
        }
    }
}

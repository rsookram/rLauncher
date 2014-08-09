package com.merono.rlauncher;

import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

public class TranslateViewDrawerListener extends DrawerLayout.SimpleDrawerListener {

    private final View mFab;
    private final int mFabTopOffset;

    private final ViewGroup mTranslateView;
    private final int mTranslateOffset;

    public TranslateViewDrawerListener(View fab, ViewGroup translateView, int translationOffset) {
        mFab = fab;
        mFabTopOffset = fab.getContext().getResources().getDimensionPixelSize(R.dimen.side_margin);

        mTranslateView = translateView;
        mTranslateOffset = translationOffset;
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        mFab.setBackgroundResource(R.drawable.fab_action_home);
    }

    @Override
    public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        mFab.setBackgroundResource(R.drawable.fab_action_all);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        mFab.setY(((mFab.getTop() - mFabTopOffset) * (1.0F - slideOffset)) + mFabTopOffset);

        mTranslateView.setX(slideOffset * mTranslateOffset);
        mTranslateView.setAlpha((1.0F - (slideOffset * 0.5F)));

        for (int i = 0; i < mTranslateView.getChildCount(); i++) {
            View v = mTranslateView.getChildAt(i);
            v.setScaleX(1.0F - (slideOffset * 0.5F));
            v.setScaleY(1.0F - (slideOffset * 0.5F));
        }
    }
}

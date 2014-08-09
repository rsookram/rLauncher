package com.merono.rlauncher;

import android.support.v4.widget.DrawerLayout;
import android.view.View;

public class TranslateViewDrawerListener extends DrawerLayout.SimpleDrawerListener {

    private final View mFab;
    private final int mFabTopOffset;

    private final View mTranslateView;
    private final int mTranslateOffset;

    public TranslateViewDrawerListener(View fab, View translateView, int translationOffset) {
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
    }
}

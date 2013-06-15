/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.home;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * The ApplicationsStackLayout is a specialized layout used for the purpose of
 * the home screen only. This layout stacks various icons in two distinct areas:
 * the favourites (or faves) and the button.
 * 
 * The layout operates from the bottom up. This means that the button area will
 * first be laid out, then the faves area.
 * 
 * The following attributes can be set in XML:
 * 
 * marginTop: the top margin of each element in the stack marginBottom: the
 * bottom margin of each element in the stack
 */
public class ApplicationsStackLayout extends ViewGroup implements
		View.OnClickListener {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private LayoutInflater mInflater;

	private int mFavoritesEnd;
	private int mFavoritesStart;

	private List<ApplicationInfo> mFavorites;

	private int mMarginTop;
	private int mMarginBottom;

	private Rect mDrawRect = new Rect();

	private Drawable mBackground;
	private int mIconSize;

	public ApplicationsStackLayout(Context context) {
		super(context);
		initLayout();
	}

	public ApplicationsStackLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.ApplicationsStackLayout);

		mMarginTop = a.getDimensionPixelSize(
				R.styleable.ApplicationsStackLayout_marginTop, 0);
		mMarginBottom = a.getDimensionPixelSize(
				R.styleable.ApplicationsStackLayout_marginBottom, 0);

		a.recycle();

		mIconSize = (int) getResources().getDimension(
				android.R.dimen.app_icon_size);

		initLayout();
	}

	private void initLayout() {
		mInflater = LayoutInflater.from(getContext());

		mBackground = getBackground();
		setBackgroundDrawable(null);
		setWillNotDraw(false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		final Drawable background = mBackground;

		final int right = getWidth();

		// Draw behind recents
		mDrawRect.set(0, 0, right, mFavoritesStart);
		background.setBounds(mDrawRect);
		background.draw(canvas);

		// Draw behind favourites
		if (mFavoritesStart > -1) {
			mDrawRect.set(0, mFavoritesStart, right, mFavoritesEnd);
			background.setBounds(mDrawRect);
			background.draw(canvas);
		}

		super.onDraw(canvas);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY
				|| heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"Can use ApplicationsStackLayout only with MeasureSpec mode=EXACTLY");
		}

		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		removeAllApplications();

		layoutVertical();
	}

	private void layoutVertical() {
		int childRight = getWidth();
		int childTop = getHeight();

		mFavoritesEnd = childTop - mMarginBottom;

		int oldChildTop = childTop;
		childTop = stackApplications(mFavorites, childRight, childTop);
		if (childTop != oldChildTop) {
			mFavoritesStart = childTop + mMarginTop;
		} else {
			mFavoritesStart = -1;
		}
	}

	private int stackApplications(List<ApplicationInfo> applications,
			int childRight, int childTop) {
		LayoutParams layoutParams;
		int widthSpec;
		int heightSpec;
		int childWidth;
		int childHeight;

		for (ApplicationInfo info : applications) {
			final View view = createApplicationIcon(mInflater, this, info);

			layoutParams = view.getLayoutParams();
			widthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width,
					MeasureSpec.EXACTLY);
			heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height,
					MeasureSpec.EXACTLY);
			view.measure(widthSpec, heightSpec);

			childWidth = view.getMeasuredWidth();
			childHeight = view.getMeasuredHeight();

			childTop -= childHeight + mMarginBottom;

			if (childTop < 0) {
				childTop += childHeight + mMarginBottom;
				break;
			}

			addViewInLayout(view, -1, layoutParams);

			view.layout(childRight - childWidth, childTop, childRight, childTop
					+ childHeight);

			childTop -= mMarginTop;
		}

		return childTop;
	}

	private void removeAllApplications() {
		for (int i = getChildCount() - 1; i >= 0; i--) {
			removeViewAt(i);
		}
	}

	private View createApplicationIcon(LayoutInflater inflater,
			ViewGroup group, ApplicationInfo info) {
		ImageView iv = (ImageView) inflater.inflate(R.layout.favorite, group,
				false);

		info.icon.setBounds(0, 0, mIconSize, mIconSize);
		iv.setImageDrawable(info.icon);

		iv.setTag(info.intent);
		iv.setOnClickListener(this);

		return iv;
	}

	public void setFavorites(List<ApplicationInfo> favApplications) {
		mFavorites = favApplications;
		requestLayout();
	}

	public void onClick(View v) {
		getContext().startActivity((Intent) v.getTag());
	}
}

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
 * the home screen only. This layout stacks various icons in three distinct
 * areas: the recents, the favorites (or faves) and the button.
 * 
 * This layout supports two different orientations: vertical and horizontal.
 * When horizontal, the areas are laid out this way:
 * 
 * [RECENTS][FAVES][BUTTON]
 * 
 * When vertical, the layout is the following:
 * 
 * [RECENTS] [FAVES] [BUTTON]
 * 
 * The layout operates from the "bottom up" (or from right to left.) This means
 * that the button area will first be laid out, then the faves area, then the
 * recents. When there are too many favorites, the recents area is not
 * displayed.
 * 
 * The following attributes can be set in XML:
 * 
 * orientation: horizontal or vertical marginLeft: the left margin of each
 * element in the stack marginTop: the top margin of each element in the stack
 * marginRight: the right margin of each element in the stack marginBottom: the
 * bottom margin of each element in the stack
 */
public class ApplicationsStackLayout extends ViewGroup implements
		View.OnClickListener {
	public static final int HORIZONTAL = 0;
	public static final int VERTICAL = 1;

	private View mButton;
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
		mButton = mInflater.inflate(R.layout.all_applications_button, this,
				false);
		addView(mButton);

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

		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final int widthSize = MeasureSpec.getSize(widthMeasureSpec);

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY
				|| heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException(
					"ApplicationsStackLayout can only be used with "
							+ "measure spec mode=EXACTLY");
		}

		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		removeAllApplications();

		LayoutParams layoutParams = mButton.getLayoutParams();
		final int widthSpec = MeasureSpec.makeMeasureSpec(layoutParams.width,
				MeasureSpec.EXACTLY);
		final int heightSpec = MeasureSpec.makeMeasureSpec(layoutParams.height,
				MeasureSpec.EXACTLY);
		mButton.measure(widthSpec, heightSpec);

		layoutVertical();
	}

	private void layoutVertical() {
		int childLeft = 0;
		int childTop = getHeight();

		int childWidth = mButton.getMeasuredWidth();
		int childHeight = mButton.getMeasuredHeight();

		childTop -= childHeight + mMarginBottom;
		mButton.layout(childLeft, childTop, childLeft + childWidth, childTop
				+ childHeight);
		childTop -= mMarginTop;
		mFavoritesEnd = childTop - mMarginBottom;

		int oldChildTop = childTop;
		childTop = stackApplications(mFavorites, childLeft, childTop);
		if (childTop != oldChildTop) {
			mFavoritesStart = childTop + mMarginTop;
		} else {
			mFavoritesStart = -1;
		}
	}

	private int stackApplications(List<ApplicationInfo> applications,
			int childLeft, int childTop) {
		LayoutParams layoutParams;
		int widthSpec;
		int heightSpec;
		int childWidth;
		int childHeight;

		final int count = applications.size();
		for (int i = count - 1; i >= 0; i--) {
			final ApplicationInfo info = applications.get(i);
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

			view.layout(childLeft, childTop, childLeft + childWidth, childTop
					+ childHeight);

			childTop -= mMarginTop;
		}

		return childTop;
	}

	private void removeAllApplications() {
		final int count = getChildCount();
		for (int i = count - 1; i >= 0; i--) {
			final View view = getChildAt(i);
			if (view != mButton) {
				removeViewAt(i);
			}
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

	/**
	 * Sets the list of favorites.
	 * 
	 * @param applications
	 *            the applications to put in the favorites area
	 */
	public void setFavorites(List<ApplicationInfo> applications) {
		mFavorites = applications;
		requestLayout();
	}

	public void onClick(View v) {
		getContext().startActivity((Intent) v.getTag());
	}
}

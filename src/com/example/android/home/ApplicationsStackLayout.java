package com.example.android.home;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * The ApplicationsStackLayout is a specialized layout used for the purpose of
 * the home screen only. This layout stacks various icons, the favourites (or
 * faves).
 * 
 * The layout operates from the bottom up.
 * 
 * The following attributes can be set in XML:
 * 
 * marginTop: the top margin of each element in the stack marginBottom: the
 * bottom margin of each element in the stack
 */
public class ApplicationsStackLayout extends ViewGroup implements
		View.OnClickListener {
	private LayoutInflater mInflater;

	private List<ApplicationInfo> mFavorites;

	private int mMarginTop;
	private int mMarginBottom;

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

		Resources res = getResources();
		mIconSize = (int) res.getDimension(android.R.dimen.app_icon_size);

		initLayout();
	}

	private void initLayout() {
		mInflater = LayoutInflater.from(getContext());

		setBackgroundDrawable(null);
		setWillNotDraw(false);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		if (widthMode != MeasureSpec.EXACTLY
				|| heightMode != MeasureSpec.EXACTLY) {
			String exMsg = "Can use ApplicationsStackLayout only with MeasureSpec mode=EXACTLY";
			throw new IllegalStateException(exMsg);
		}

		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(widthSize, heightSize);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		removeAllApplications();
		stackApplications(mFavorites, getWidth(), getHeight());
	}

	private void stackApplications(List<ApplicationInfo> applications,
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

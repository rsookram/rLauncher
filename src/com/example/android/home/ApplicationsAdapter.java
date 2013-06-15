package com.example.android.home;

import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * GridView adapter to show the list of all installed applications.
 */
public class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {
	private Activity activity;
	private ArrayList<ApplicationInfo> mApplications;

	private Rect mOldBounds = new Rect();

	public ApplicationsAdapter(Activity activity,
			ArrayList<ApplicationInfo> apps) {
		super(activity, 0, apps);
		this.activity = activity;
		this.mApplications = apps;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ApplicationInfo info = mApplications.get(position);

		if (convertView == null) {
			final LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.application, parent, false);
		}

		Drawable ic = info.icon;

		if (!info.filtered) {
			final Resources resources = activity.getResources();
			int width = 64;// (int)
							// resources.getDimension(android.R.dimen.app_icon_size);
			int height = 64;// (int)
							// resources.getDimension(android.R.dimen.app_icon_size);

			final int iconWidth = ic.getIntrinsicWidth();
			final int iconHeight = ic.getIntrinsicHeight();

			if (ic instanceof PaintDrawable) {
				PaintDrawable painter = (PaintDrawable) ic;
				painter.setIntrinsicWidth(width);
				painter.setIntrinsicHeight(height);
			}

			if (width < iconWidth || height < iconHeight) {
				float ratio = (float) iconWidth / iconHeight;

				if (iconWidth > iconHeight) {
					height = (int) (width / ratio);
				} else if (iconHeight > iconWidth) {
					width = (int) (height * ratio);
				}

				Bitmap.Config c = ic.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
						: Bitmap.Config.RGB_565;
				Bitmap thumb = Bitmap.createBitmap(width, height, c);
				Canvas canvas = new Canvas(thumb);
				canvas.setDrawFilter(new PaintFlagsDrawFilter(
						Paint.DITHER_FLAG, 0));
				// Copy the old bounds to restore them later
				// If we were to do oldBounds = icon.getBounds(),
				// the call to setBounds() that follows would
				// change the same instance and we would lose the
				// old bounds
				mOldBounds.set(ic.getBounds());
				ic.setBounds(0, 0, width, height);
				ic.draw(canvas);
				ic.setBounds(mOldBounds);
				ic = info.icon = new BitmapDrawable(resources, thumb);
				info.filtered = true;
			}
		}

		((ImageView) convertView.findViewById(R.id.img)).setImageDrawable(ic);

		return convertView;
	}
}
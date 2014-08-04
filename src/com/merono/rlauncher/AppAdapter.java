package com.merono.rlauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/** GridView adapter to show the list of all installed applications. */
public class AppAdapter extends ArrayAdapter<AppInfo> {

    private final LayoutInflater mInflater;

    public AppAdapter(Context context, List<AppInfo> apps) {
        super(context, /* Not used */ -1, apps);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.application, parent, false);
        }

        Drawable ic = getItem(position).icon;
        ((ImageView) convertView).setImageDrawable(ic);

        return convertView;
    }
}

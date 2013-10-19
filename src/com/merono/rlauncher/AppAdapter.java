package com.merono.rlauncher;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.merono.rlauncher.R;

import java.util.List;

/** GridView adapter to show the list of all installed applications. */
public class AppAdapter extends ArrayAdapter<AppInfo> {

    private LayoutInflater mInflater;

    public AppAdapter(Context context, List<AppInfo> apps) {
        super(context, 0, apps);
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.application, parent, false);
        }

        Drawable ic = getItem(position).icon;
        ((ImageView) convertView.findViewById(R.id.img)).setImageDrawable(ic);

        return convertView;
    }
}

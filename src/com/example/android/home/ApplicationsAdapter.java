package com.example.android.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/** GridView adapter to show the list of all installed applications. */
public class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {

    private LayoutInflater mInflater;

    public ApplicationsAdapter(Context context, List<ApplicationInfo> apps) {
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
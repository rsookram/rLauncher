package com.example.android.home;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/** GridView adapter to show the list of all installed applications. */
public class ApplicationsAdapter extends ArrayAdapter<ApplicationInfo> {

    private Activity activity;
    private List<ApplicationInfo> mApplications;

    public ApplicationsAdapter(Activity activity,
                               List<ApplicationInfo> apps) {
        super(activity, 0, apps);
        this.activity = activity;
        this.mApplications = apps;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            convertView = inflater.inflate(R.layout.application, parent, false);
        }

        Drawable ic = mApplications.get(position).icon;
        ((ImageView) convertView.findViewById(R.id.img)).setImageDrawable(ic);

        return convertView;
    }
}
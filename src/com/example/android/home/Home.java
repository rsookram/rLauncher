package com.example.android.home;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Home extends Activity {

    private static final String LOG_TAG = "Home";

    private static final String FAVORITES_PATH = "favourites.json";
    public static final int APP_LAUNCH_FLAGS = Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

    /** Width and height of bounds of favourite icons in px */
    private int iconSize;

    private static List<ApplicationInfo> mApplications;
    private static List<ApplicationInfo> mFavorites;

    // UI components
    private DrawerLayout mDrawerLayout;
    private GridView mGridDrawer;

    private LinearLayout mApplicationsStack;

    private DrawerLayout.DrawerListener mDrawerListener;

    /** Receives notifications when applications are added/removed. */
    private final BroadcastReceiver mApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApplications(false);
            bindApplications();
            bindFavorites(false);
        }
    };

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.home);

        Resources res = getResources();
        iconSize = (int) res.getDimension(android.R.dimen.app_icon_size);

        registerIntentReceiver();

        loadApplications(true);

        bindApplications();
        bindFavorites(true);

        mDrawerListener = new TranslateViewDrawerListener(mApplicationsStack, iconSize);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerListener);

        mGridDrawer.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                final ApplicationInfo app = (ApplicationInfo) parent.getItemAtPosition(pos);

                mDrawerLayout.setDrawerListener(new TranslateViewDrawerListener(mApplicationsStack, iconSize) {
                    @Override
                    public void onDrawerStateChanged(int newState) {
                        super.onDrawerStateChanged(newState);
                        // This is here rather than in onDrawerClosed to handle
                        // the case where the drawer is "caught" while closing
                        // and is brought to the opened state. This is
                        // interpreted as canceling the item selection.
                        if (newState == DrawerLayout.STATE_IDLE) {
                            mDrawerLayout.setDrawerListener(mDrawerListener);
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        startActivity(app.intent);
                    }
                });
                mDrawerLayout.closeDrawer(mGridDrawer);
            }
        });
    }

    /** Creates a new applications adapter for the grid view and registers it. */
    private void bindApplications() {
        if (mApplicationsStack == null) {
            mApplicationsStack = (LinearLayout) findViewById(R.id.faves);
        }

        mGridDrawer = (GridView) findViewById(R.id.left_drawer);
        mGridDrawer.setAdapter(new ApplicationsAdapter(this, mApplications));
    }

    /**
     * Refreshes the favourite applications stacked. The number of favourites
     * depends on the user.
     */
    private void bindFavorites(boolean isLaunching) {
        if (!isLaunching || mFavorites == null) {
            if (mFavorites == null) {
                mFavorites = new LinkedList<ApplicationInfo>();
            } else {
                mFavorites.clear();
            }

            InputStream is;
            String appsString;
            try {
                is = getAssets().open(FAVORITES_PATH);
                appsString = convertStreamToString(is);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Couldn't find or open favourites file", e);
                return;
            }

            JSONArray array;
            try {
                array = new JSONArray(appsString);
            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            final PackageManager pm = getPackageManager();
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    ApplicationInfo app = new ApplicationInfo(object, pm, APP_LAUNCH_FLAGS);
                    mFavorites.add(0, app);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        for (ApplicationInfo info : mFavorites) {
            ImageView iv = (ImageView) getLayoutInflater().inflate(
                    R.layout.favorite, mApplicationsStack, false);

            info.icon.setBounds(0, 0, iconSize, iconSize);
            iv.setImageDrawable(info.icon);

            iv.setTag(info.intent);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity((Intent) view.getTag());
                }
            });
            mApplicationsStack.addView(iv, 0);
        }
    }

    /** Loads the list of installed applications in mApplications. */
    private void loadApplications(boolean isLaunching) {
        if (isLaunching && mApplications != null) {
            return;
        }

        PackageManager manager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);

        if (apps != null) {
            Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
            if (mApplications == null) {
                mApplications = new ArrayList<ApplicationInfo>(apps.size());
            }
            mApplications.clear();

            for (ResolveInfo info : apps) {
                ApplicationInfo application = new ApplicationInfo();

                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name), APP_LAUNCH_FLAGS);
                application.icon = info.activityInfo.loadIcon(manager);

                mApplications.add(application);
            }
        }
    }

    /** Registers intent receiver for when apps are (un)installed */
    private void registerIntentReceiver() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
        filter.addDataScheme("package");
        registerReceiver(mApplicationsReceiver, filter);
    }

    @Override
    public void onBackPressed() {
        // intentionally don't call super so that the launcher isn't closed
        // when back is pressed
        mDrawerLayout.closeDrawer(mGridDrawer);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Remove the callback for the cached drawables or we leak the previous
        // Home screen on orientation change
        for (ApplicationInfo application : mApplications) {
            application.icon.setCallback(null);
        }

        unregisterReceiver(mApplicationsReceiver);
    }

    public static String convertStreamToString(InputStream is)
            throws IOException {
        StringWriter writer = new StringWriter();

        char[] buffer = new char[2048];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is,
                    "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }
}

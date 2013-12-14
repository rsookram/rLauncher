package com.merono.rlauncher;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class Home extends Activity {

    public static final int APP_LAUNCH_FLAGS = Intent.FLAG_ACTIVITY_NEW_TASK
            | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

    /** Width and height of bounds of favourite icons in px */
    private int iconSize;

    private static List<AppInfo> mAllApps;
    private static List<AppInfo> mFavorites;

    // UI components
    private FixedOpenDrawerLayout mDrawerLayout;
    private GridView mGridDrawer;

    private LinearLayout mAppsStack;

    private FixedOpenDrawerLayout.DrawerListener mDrawerListener;

    /** Receives notifications when applications are added/removed. */
    private final BroadcastReceiver mApplicationsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadApplications(false);
            bindApplications();
            bindFavorites();
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
        bindFavorites();

        mDrawerListener = new TranslateViewDrawerListener(mAppsStack, iconSize);

        mDrawerLayout = (FixedOpenDrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerListener);

        mGridDrawer.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
                final AppInfo app = (AppInfo) parent.getItemAtPosition(pos);

                mDrawerLayout.setDrawerListener(new TranslateViewDrawerListener(mAppsStack, iconSize) {
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

        mGridDrawer.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo app = (AppInfo) parent.getItemAtPosition(position);
                try {
                    String appString = app.toJSON().toString();
                    ClipData data = ClipData.newPlainText("info", appString);
                    view.startDrag(data, new View.DragShadowBuilder(view), null, 0);
                    mDrawerLayout.closeDrawer(mGridDrawer);
                    return true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        });

        mAppsStack.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    ClipData data = event.getClipData();
                    ClipData.Item item = data.getItemAt(0);
                    String jsonString = item.getText().toString();
                    try {
                        mFavorites.add(new AppInfo(new JSONObject(jsonString), getPackageManager(), APP_LAUNCH_FLAGS));
                        SavedAppsHelper.saveApps(Home.this, mFavorites);
                        bindFavorites();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });
    }

    /** Creates a new applications adapter for the grid view and registers it. */
    private void bindApplications() {
        mAppsStack = (LinearLayout) findViewById(R.id.faves);

        mGridDrawer = (GridView) findViewById(R.id.left_drawer);
        mGridDrawer.setAdapter(new AppAdapter(this, mAllApps));
    }

    /** Refreshes the favourite applications stacked. */
    private void bindFavorites() {
        if (mFavorites == null) {
            mFavorites = new LinkedList<AppInfo>();
        } else {
            mFavorites.clear();
            mAppsStack.removeAllViews();
        }

        List<AppInfo> savedApps = SavedAppsHelper.getSavedApps(this);
        if (savedApps != null) {
        	mFavorites.addAll(savedApps);
        }

        LayoutInflater inflater = getLayoutInflater();
        for (AppInfo info : mFavorites) {
            ImageView iv = (ImageView) inflater.inflate(R.layout.favorite,
            		mAppsStack, false);

            info.icon.setBounds(0, 0, iconSize, iconSize);
            iv.setImageDrawable(info.icon);

            iv.setTag(info.intent);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity((Intent) view.getTag());
                }
            });
            mAppsStack.addView(iv, 0);
        }
    }

    /** Loads the list of installed applications in mApplications. */
    private void loadApplications(boolean isLaunching) {
        if (isLaunching && mAllApps != null) {
            return;
        }

        PackageManager manager = getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);

        if (apps != null) {
            Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));
            if (mAllApps == null) {
                mAllApps = new ArrayList<AppInfo>(apps.size());
            }
            mAllApps.clear();

            for (ResolveInfo info : apps) {
                AppInfo application = new AppInfo();

                application.setActivity(new ComponentName(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name), APP_LAUNCH_FLAGS);
                application.icon = info.activityInfo.loadIcon(manager);

                mAllApps.add(application);
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
        for (AppInfo application : mAllApps) {
            application.icon.setCallback(null);
        }

        unregisterReceiver(mApplicationsReceiver);
    }
}

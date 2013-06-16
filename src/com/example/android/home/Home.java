package com.example.android.home;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class Home extends Activity {
	private static final String LOG_TAG = "Home";

	private static final String FAVORITES_PATH = "favourites.xml";

	private static final String TAG_FAVORITES = "favorites";
	private static final String TAG_FAVORITE = "favorite";
	private static final String TAG_PACKAGE = "package";
	private static final String TAG_CLASS = "class";

	private static List<ApplicationInfo> mApplications;
	private static List<ApplicationInfo> mFavorites;

	private final BroadcastReceiver mApplicationsReceiver = new ApplicationsIntentReceiver();

	private DrawerLayout mDrawerLayout;
	private GridView mGridDrawer;

	private ApplicationsStackLayout mApplicationsStack;

	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.home);

		registerIntentReceiver();

		loadApplications(true);

		bindApplications();
		bindFavorites(true);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mGridDrawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View v, int position,
					long id) {
				ApplicationInfo app = (ApplicationInfo) parent
						.getItemAtPosition(position);
				startActivity(app.intent);
				mDrawerLayout.closeDrawer(mGridDrawer);
			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Remove the callback for the cached drawables or we leak the previous
		// Home screen on orientation change
		for (int i = 0; i < mApplications.size(); i++) {
			mApplications.get(i).icon.setCallback(null);
		}

		unregisterReceiver(mApplicationsReceiver);
	}

	/**
	 * Registers intent receiver for when apps are (un)installed
	 */
	private void registerIntentReceiver() {
		IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("package");
		registerReceiver(mApplicationsReceiver, filter);
	}

	/**
	 * Creates a new applications adapter for the grid view and registers it.
	 */
	private void bindApplications() {
		if (mApplicationsStack == null) {
			mApplicationsStack = (ApplicationsStackLayout) findViewById(R.id.faves);
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

			InputStream is = null;
			try {
				is = getAssets().open(FAVORITES_PATH);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Couldn't find or open favourites file");
				return;
			}

			try {
				final XmlPullParser parser = Xml.newPullParser();
				parser.setInput(is, "UTF-8");

				beginDocument(parser, TAG_FAVORITES);

				final PackageManager packageManager = getPackageManager();

				while (true) {
					nextElement(parser);
					String name = parser.getName();
					if (!TAG_FAVORITE.equals(name)) {
						break;
					}

					String packageName = parser.getAttributeValue(null,
							TAG_PACKAGE);
					String className = parser
							.getAttributeValue(null, TAG_CLASS);
					ComponentName cn = new ComponentName(packageName, className);

					final Intent intent = new Intent(Intent.ACTION_MAIN, null);
					intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setComponent(cn);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

					ApplicationInfo application = getApplicationInfo(
							packageManager, intent);
					if (application != null) {
						application.intent = intent;

						mFavorites.add(0, application);
					}
				}
			} catch (XmlPullParserException e) {
				Log.w(LOG_TAG, "Got exception parsing favorites.", e);
			} catch (IOException e) {
				Log.w(LOG_TAG, "Got exception parsing favorites.", e);
			}
		}

		mApplicationsStack.setFavorites(mFavorites);
	}

	private static void beginDocument(XmlPullParser parser,
			String firstElementName) throws XmlPullParserException, IOException {

		int type;
		while ((type = parser.next()) != XmlPullParser.START_TAG
				&& type != XmlPullParser.END_DOCUMENT) {
			// Empty
		}

		if (type != XmlPullParser.START_TAG) {
			throw new XmlPullParserException("No start tag found");
		}

		if (!parser.getName().equals(firstElementName)) {
			throw new XmlPullParserException("Unexpected start tag: found "
					+ parser.getName() + ", expected " + firstElementName);
		}
	}

	private static void nextElement(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		int type;
		while ((type = parser.next()) != XmlPullParser.START_TAG
				&& type != XmlPullParser.END_DOCUMENT) {
			// Empty
		}
	}

	private static ApplicationInfo getApplicationInfo(PackageManager manager,
			Intent intent) {
		final ResolveInfo resolveInfo = manager.resolveActivity(intent, 0);

		if (resolveInfo == null) {
			return null;
		}

		final ApplicationInfo info = new ApplicationInfo();
		final ActivityInfo activityInfo = resolveInfo.activityInfo;
		info.icon = activityInfo.loadIcon(manager);
		if (info.title == null || info.title.length() == 0) {
			info.title = activityInfo.loadLabel(manager);
		}
		if (info.title == null) {
			info.title = "";
		}
		return info;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_UP) {
			if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
				if (!event.isCanceled()) {
					mDrawerLayout.closeDrawer(mGridDrawer);
				}
				return true;
			}
		}
		return super.dispatchKeyEvent(event);
	}

	/**
	 * Loads the list of installed applications in mApplications.
	 */
	private void loadApplications(boolean isLaunching) {
		if (isLaunching && mApplications != null) {
			return;
		}

		PackageManager manager = getPackageManager();

		Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

		final List<ResolveInfo> apps = manager.queryIntentActivities(
				mainIntent, 0);
		Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

		if (apps != null) {
			if (mApplications == null) {
				mApplications = new ArrayList<ApplicationInfo>(apps.size());
			}
			mApplications.clear();

			for (ResolveInfo info : apps) {
				ApplicationInfo application = new ApplicationInfo();

				application.title = info.loadLabel(manager);
				application.setActivity(new ComponentName(
						info.activityInfo.applicationInfo.packageName,
						info.activityInfo.name), Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				application.icon = info.activityInfo.loadIcon(manager);

				mApplications.add(application);
			}
		}
	}

	/**
	 * Receives notifications when applications are added/removed.
	 */
	private class ApplicationsIntentReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			loadApplications(false);
			bindApplications();
			bindFavorites(false);
		}
	}
}

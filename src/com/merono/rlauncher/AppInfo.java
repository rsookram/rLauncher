package com.merono.rlauncher;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

import org.json.JSONException;
import org.json.JSONObject;

/** Represents a launchable application. */
public class AppInfo {

    // Keys used in the JSON representation of this class
    private static final String KEY_PACKAGE = "package";
    private static final String KEY_CLASS = "class";

    public Intent intent;

    public Drawable icon;

    public AppInfo() {
    }

    public AppInfo(JSONObject obj, PackageManager manager, int launchFlags)
            throws JSONException {
        String packageName = obj.getString(KEY_PACKAGE);
        String className = obj.getString(KEY_CLASS);

        ComponentName cn = new ComponentName(packageName, className);
        setActivity(cn, launchFlags);

        ResolveInfo resolveInfo = manager.resolveActivity(intent, 0);
        if (resolveInfo == null) {
        	icon = null;
        } else {
        	icon = resolveInfo.activityInfo.loadIcon(manager);
        }
    }

    /**
     * Creates the application intent based on a component name and various
     * launch flags.
     *
     * @param className class name of the component representing the intent
     */
    final void setActivity(ComponentName className, int launchFlags) {
        intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(className);
        intent.setFlags(launchFlags);
    }

    public JSONObject toJSON() throws JSONException {
        String packageName = intent.getComponent().getPackageName();
        String className = intent.getComponent().getClassName();

        JSONObject obj = new JSONObject();
        obj.put(KEY_PACKAGE, packageName);
        obj.put(KEY_CLASS, className);

        return obj;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AppInfo)) {
            return false;
        }

        AppInfo that = (AppInfo) o;
        return intent.getComponent().getClassName()
                .equals(that.intent.getComponent().getClassName());
    }

    @Override
    public int hashCode() {
        String name = intent.getComponent().getClassName();
        return name != null ? name.hashCode() : 0;
    }
}

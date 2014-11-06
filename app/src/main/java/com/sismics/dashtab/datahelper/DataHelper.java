package com.sismics.dashtab.datahelper;

import android.content.Context;

/**
 * @author bgamard.
 */
public abstract class DataHelper {
    protected Context context;

    public DataHelper(Context context) {
        this.context = context;
    }

    public abstract void onCreate();

    public abstract  void onDestroy();
}

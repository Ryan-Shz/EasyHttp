package com.github.ryan.easyhttp.lifecycle;

/**
 * An interface for listener to {@link android.app.Fragment} and {@link android.app.Activity}
 * lifecycle events.
 */
public interface LifecycleListener {

    /**
     * Callback for when {@link android.app.Fragment#onDestroy()}} or {@link
     * android.app.Activity#onDestroy()} is called.
     */
    void onDestroy();
}

package com.github.ryan.easyhttp.lifecycle;

import android.support.annotation.NonNull;

import com.github.ryan.easyhttp.utils.Utils;

import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

/**
 * A {@link Lifecycle} implementation for tracking and notifying
 * listeners of {@link android.app.Fragment} and {@link android.app.Activity} lifecycle events.
 */
class ActivityFragmentLifecycle implements Lifecycle {

    private final Set<LifecycleListener> lifecycleListeners =
            Collections.newSetFromMap(new WeakHashMap<LifecycleListener, Boolean>());
    private boolean isDestroyed;

    /**
     * Adds the given listener to the list of listeners to be notified on each lifecycle event.
     * <p>
     * <p> Note - {@link LifecycleListener}s that are added more than once
     * will have their lifecycle methods called more than once. It is the caller's responsibility to
     * avoid adding listeners multiple times. </p>
     */
    @Override
    public void addListener(@NonNull LifecycleListener listener) {
        lifecycleListeners.add(listener);
        if (isDestroyed) {
            listener.onDestroy();
        }
    }

    @Override
    public void removeListener(@NonNull LifecycleListener listener) {
        lifecycleListeners.remove(listener);
    }

    void onDestroy() {
        isDestroyed = true;
        for (LifecycleListener lifecycleListener : Utils.getSnapshot(lifecycleListeners)) {
            lifecycleListener.onDestroy();
        }
        lifecycleListeners.clear();
    }
}

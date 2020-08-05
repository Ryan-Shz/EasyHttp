package com.github.ryan.easyhttp.lifecycle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;


import com.github.ryan.easyhttp.EasyHttp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求生命周期自动管理入口
 * <p>
 * Created by Ryan
 * on 2020/7/29
 */
public class LifecycleRegistry {

    private static final String TAG = LifecycleRegistry.class.getSimpleName();
    private static final String FRAGMENT_TAG = "com.easy.http.manager";
    private static final Map<FragmentManager, RequestManagerFragment> sPendingRequestManagerFragments = new ConcurrentHashMap<>();

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static void assertNotDestroyed(@NonNull Activity activity) {
        if (activity.isFinishing() || activity.isDestroyed()) {
            throw new IllegalArgumentException("You cannot start a load for a destroyed activity");
        }
    }

    public static void handle(View view, final EasyHttp http) {
        Context context = view.getContext();
        handle(context, http);
    }

    public static void handle(Fragment fragment, final EasyHttp http) {
        if (fragment == null) {
            Log.e(TAG, "fragment is null, can't bind lifecycle.");
            return;
        }
        handle(fragment.getActivity(), http);
    }

    public static void handle(Context context, final EasyHttp http) {
        if (context == null) {
            throw new IllegalArgumentException("You cannot start a load on a null Context");
        }
        if (context instanceof FragmentActivity) {
            handle((FragmentActivity) context, http);
        } else if (context instanceof Activity) {
            handle((Activity) context, http);
        } else if (context instanceof ContextWrapper) {
            handle(((ContextWrapper) context).getBaseContext(), http);
        } else {
            Log.d(TAG, "context is invalid, do nothing.");
        }
    }

    public static void handle(Activity activity, final EasyHttp http) {
        if (activity == null) {
            Log.e(TAG, "activity is null, can't bind lifecycle.");
            return;
        }
        assertNotDestroyed(activity);
        FragmentManager fm = activity.getFragmentManager();
        RequestManagerFragment current = (RequestManagerFragment) fm.findFragmentByTag(FRAGMENT_TAG);
        if (current == null) {
            current = sPendingRequestManagerFragments.get(fm);
            if (current == null) {
                current = new RequestManagerFragment();
                sPendingRequestManagerFragments.put(fm, current);
            }
            fm.beginTransaction().add(current, FRAGMENT_TAG).commitAllowingStateLoss();
        }
        final ActivityFragmentLifecycle lifecycle = current.getHttpLifecycle();
        lifecycle.addListener(new LifecycleListener() {
            @Override
            public void onDestroy() {
                EasyHttp.cancelByTag(http.getTag());
                lifecycle.removeListener(this);
            }
        });
    }
}

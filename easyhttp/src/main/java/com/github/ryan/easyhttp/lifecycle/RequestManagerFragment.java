package com.github.ryan.easyhttp.lifecycle;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public class RequestManagerFragment extends Fragment {

    private final ActivityFragmentLifecycle lifecycle;

    public RequestManagerFragment() {
        this(new ActivityFragmentLifecycle());
    }

    @VisibleForTesting
    @SuppressLint("ValidFragment")
    RequestManagerFragment(@NonNull ActivityFragmentLifecycle lifecycle) {
        this.lifecycle = lifecycle;
    }

    @NonNull
    ActivityFragmentLifecycle getHttpLifecycle() {
        return lifecycle;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycle.onDestroy();
    }
}

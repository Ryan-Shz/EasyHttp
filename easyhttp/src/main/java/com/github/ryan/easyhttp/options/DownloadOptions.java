package com.github.ryan.easyhttp.options;

import com.github.ryan.easyhttp.callback.DownloadCallback;

import java.io.File;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public class DownloadOptions {

    private File mTargetFile;
    private DownloadCallback mCallback;

    public File getTargetFile() {
        return mTargetFile;
    }

    public void setTargetFile(File targetFile) {
        this.mTargetFile = targetFile;
    }

    public DownloadCallback getCallback() {
        return mCallback;
    }

    public void setCallback(DownloadCallback callback) {
        this.mCallback = callback;
    }
}

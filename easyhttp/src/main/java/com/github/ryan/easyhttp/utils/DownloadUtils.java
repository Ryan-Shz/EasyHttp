package com.github.ryan.easyhttp.utils;

import com.github.ryan.easyhttp.callback.DownloadCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by Ryan
 * at 2019/9/23
 */
public class DownloadUtils {

    public static void writeToFile(ResponseBody responseBody, File targetFile, DownloadCallback callback) throws IOException {
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            boolean supportProgress = true;
            is = responseBody.byteStream();
            fos = new FileOutputStream(targetFile);
            long contentLength = responseBody.contentLength();
            if (contentLength <= 0) {
                supportProgress = false;
            }
            byte[] buffer = new byte[2048];
            int len;
            int progress;
            long currentBytes = 0;
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                currentBytes += len;
                if (supportProgress) {
                    progress = (int) ((currentBytes * 1.0 / contentLength) * 100);
                    if (callback != null) {
                        callback.onProgress(progress);
                    }
                }
            }
            fos.flush();
            if (callback != null && !supportProgress) {
                callback.onProgress(100);
            }
        } finally {
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
        }
    }
}

package com.github.ryan.easyhttp.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.github.ryan.easyhttp.EasyHttpActivity;
import com.github.ryan.easyhttp.callback.DownloadCallback;
import com.github.ryan.easyhttp.callback.HttpCallback;

import java.io.File;
import retrofit2.Response;

/**
 * Created by Ryan
 * at 2019/9/20
 */
public class MainActivity extends EasyHttpActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TEST_API_URL = "http://ajax.googleapis.com/ajax/services/feed/load?q=http://www.bilibili.tv/rss-13.xml&v=1.0";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button testBtn1 = findViewById(R.id.test_btn1);
        Button testBtn2 = findViewById(R.id.test_btn2);
        Button testBtn3 = findViewById(R.id.test_btn3);
        Button testBtn4 = findViewById(R.id.test_btn4);

        testBtn1.setOnClickListener(v -> asyncRequestTest());
        testBtn2.setOnClickListener(v -> syncRequestTest());
        testBtn3.setOnClickListener(v -> downloadFileAsync());
        testBtn4.setOnClickListener(v -> downloadFileSync());
    }

    private void asyncRequestTest() {
        TestHttp.target(TestResult.class)
                .setFullUrl(TEST_API_URL)
                .bindLifecycle(this)
                .addParam("key1", "value1")
                .addParam("key2", "value2")
                .setResultConverter(new TestResultConverter<>(Response.class))
                .get(new HttpCallback<TestResult>() {
                    @Override
                    public void onSuccess(TestResult result) {
                        super.onSuccess(result);
                        toast(result.getResponseDetails());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);
                        toast(e.getMessage());
                    }
                });
    }

    private void syncRequestTest() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                String result = TestHttp.target(String.class)
                        .setFullUrl(TEST_API_URL)
                        .addParam("key1", "value1")
                        .addParam("key2", "value2")
                        .sync()
                        .get();
                toast(result);
            }
        }.start();
    }

    private void downloadFileAsync() {
        File targetFile = new File(getCacheDir(), "test");
        TestHttp.target(File.class)
                .targetFile(targetFile)
                .setFullUrl("http://pic18.nipic.com/20111223/2457331_222522032324_2.jpg")
                .download(new DownloadCallback<File>() {

                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(File file) {
                        super.onSuccess(file);
                        toast("download success: " + file.getPath());
                    }

                    @Override
                    public void onFailure(Throwable e) {
                        super.onFailure(e);
                        toast("download failed: " + e.getMessage());
                    }

                    @Override
                    public void onProgress(int progress) {
                        super.onProgress(progress);
                        Log.i(TAG, "progress: " + progress);
                    }
                });
    }

    private void downloadFileSync() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                File targetFile = new File(getCacheDir(), "test");
                File file = TestHttp.target(File.class)
                        .targetFile(targetFile)
                        .setFullUrl("http://pic18.nipic.com/20111223/2457331_222522032324_2.jpg")
                        .sync()
                        .download();
                if (file != null) {
                    toast("download success: " + file.getPath());
                } else {
                    toast("download failed");
                }
            }
        }.start();
    }

    private void toast(final String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

}
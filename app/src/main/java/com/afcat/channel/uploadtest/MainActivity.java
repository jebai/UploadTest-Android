package com.afcat.channel.uploadtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onUpload(View view) {

        OkHttpClient client = new OkHttpClient.Builder().connectionPool(new ConnectionPool(5, 5, TimeUnit.MINUTES)).build();

        File file = new File("/sdcard/a.txt");
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fileData", file.getName(), RequestBody.create(MediaType.parse("application/zip"), file))
                .build();
        Request request = new Request.Builder()
                .url("http://10.0.0.150:3000/api/containers/container1/upload")
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("123", Log.getStackTraceString(e));
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "onResponse" + response.code(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}

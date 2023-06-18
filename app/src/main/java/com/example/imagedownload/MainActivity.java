package com.example.imagedownload;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity implements Handler.Callback {
    public static final String KEY_HANDLER_MSG = "status";
    private static final String IMAGE_SOURCE = "https://lh3.googleusercontent.com/ElYRviiTJBpugDjLjSp9Lnc6Uz1xOfod4DbNRDsoDTZO2NnBkgarFf7NuYL1Oc7VD2irduGPYd6oDsWzEeICh_1xKsKQ9BP9Ci1hK9ZgilBz9ekV4_U8=w6000-rw-e365-v1";
    private Button btnDownloadFile;
    private Button btnDownloadFileAsync;
    private TextView statusTextView;
    private ImageView imageView;
    // declare handler
    private Handler handler;

    private Runnable imageDownloader;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDownloadFile = (Button) findViewById(R.id.btnDownloadFile);
        btnDownloadFileAsync = (Button) findViewById(R.id.btnDownloadFileAsync);
        imageView = (ImageView) findViewById(R.id.image_view);
        statusTextView = (TextView) findViewById(R.id.status);

        // exercise 1 - step 1
        btnDownloadFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(imageDownloader, "Download thread").start();
                statusTextView.setText(getString(R.string.download_started));
            }
        });

        // exercise 1 - step 1
        imageDownloader = new Runnable() {
            public void run() {
                // exercise 1 - step 1
                downloadImage(IMAGE_SOURCE);
            }
        };

        // exercise 1 - step 2 to implement
        btnDownloadFileAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadTask downloadTask = new DownloadTask((ImageView) findViewById(R.id.image_view));
                downloadTask.execute(IMAGE_SOURCE);
            }
        });

        // initialize handler
        handler = new Handler(this);
    }

    private void downloadImage(String urlStr){
        try {
//            URL imageUrl = new URL("https", "android.com", "/images/froyo.png");
            URL imageUrl = new URL(urlStr);
            Bitmap image = BitmapFactory.decodeStream(imageUrl.openStream());
            if (image != null) {
                Log.i("DL", getString(R.string.download_success));
                sendMessage(getString(R.string.download_success));
            } else {
                Log.i("DL", getString(R.string.download_failed_stream));
                sendMessage(getString(R.string.download_failed_stream));
            }
        } catch (Exception e) {
            Log.i("DL", getString(R.string.download_failed));
            sendMessage(getString(R.string.download_failed));
            e.printStackTrace();
        }
    }

    @Override
    public boolean handleMessage(@NonNull Message message) {
        String text = message.getData().getString("status");
        TextView statusText = (TextView) findViewById(R.id.status);
        statusText.setText(text);
        return false;
    }

    private void sendMessage(String what) {
        Bundle bundle = new Bundle();
        bundle.putString("status", what);
        Message message = new Message();
        message.setData(bundle);
        handler.sendMessage(message);
    }
}

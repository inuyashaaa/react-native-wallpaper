package com.kaislibrary;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.WritableMap;

import javax.annotation.Nullable;

public class RNWalleModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    private Callback rctCallback = null;
    Context context;
    String imageurl;
    String message = null;
    public RNWalleModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
        context = reactContext.getApplicationContext();
    }

    @Override
    public String getName() {
        return "RNWalle";
    }

    @ReactMethod
    public void setWallPaper(final String imgUri, @Nullable final String dest, Callback callback) {
        rctCallback = callback;
        imageurl = imgUri;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                message = "failed";
                try  {
                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context);

                    try {
                        if(imageurl.startsWith("http://") || imageurl.startsWith("https://")) {
                            URL url = new URL(imageurl);
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap bitmap = BitmapFactory.decodeStream(input);
                            if (bitmap != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (getWallpaperDestination(dest) == 2) {
                                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                    }
                                    else {
                                        myWallpaperManager.setBitmap(bitmap, null, true, getWallpaperDestination(dest));
                                    }
                                }
                                else{
                                    myWallpaperManager.setBitmap(bitmap);
                                }
                                message = "success";
                            }
                            rctCallback.invoke(message);
                        } else if(imageurl.startsWith("file://")) {
                            Bitmap bitmap = BitmapFactory.decodeFile(imageurl.replace("file://",""));
                            if (bitmap != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    if (getWallpaperDestination(dest) == 2) {
                                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                                        myWallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                                    }
                                    else {
                                        myWallpaperManager.setBitmap(bitmap, null, true, getWallpaperDestination(dest));
                                    }
                                }
                                else{
                                    myWallpaperManager.setBitmap(bitmap);
                                }
                                message = "success";
                            }
                            rctCallback.invoke(message);
                        }
                    } catch (Exception e) {
                        message = e.getMessage();
                        rctCallback.invoke(message);
                    }
                } catch (Exception e) {
                    message = e.getMessage();
                    rctCallback.invoke(message);
                }
            }
        });
        thread.start();
    }

    private int getWallpaperDestination(String dest){
        switch (dest) {
            case "both":
                return 2;
            case "lock":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return WallpaperManager.FLAG_LOCK;
                }
                return 0;
            case "system":
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return WallpaperManager.FLAG_SYSTEM;
                }
                return 0;
            default:
                return 0;
        }
    }
}

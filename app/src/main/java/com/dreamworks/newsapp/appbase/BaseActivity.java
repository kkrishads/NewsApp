package com.dreamworks.newsapp.appbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dreamworks.newsapp.NewsApp;


public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NewsApp getApp() {
        return (NewsApp) getApplication();
    }
}
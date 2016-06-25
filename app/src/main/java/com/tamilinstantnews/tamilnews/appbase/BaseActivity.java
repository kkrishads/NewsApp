package com.tamilinstantnews.tamilnews.appbase;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tamilinstantnews.tamilnews.NewsApp;


public class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public NewsApp getApp() {
        return (NewsApp) getApplication();
    }
}
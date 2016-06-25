package com.tamilinstantnews.tamilnews;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilinstantnews.tamilnews.appbase.BaseActivity;
import com.tamilinstantnews.tamilnews.utils.DeviceUtils;
import com.tamilinstantnews.tamilnews.utils.MTProgressDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvFeedList;
    private MTProgressDialog progressDialog;
    private ArrayList<NewsItems> feedBeanList;
    private NewsListAdapter feedListAdapter;
    private TextView txtTryAgain;
    public static final String KEY_ITEM = HomeActivity.class.getPackage().getName() + ".KEY_ITEM";
    AsyncLatestNews asyncLatestNews;
    private Boolean isFabOpen = false;
    private FloatingActionButton fabMore, fabShare, fabRate;
    private Animation fabMoreOpen, fabMoreClose, rotateForward, rotateBackward;
    private Calendar mDate;
    private long mSelectedDate = 0;
    int count = 1, lastVisibleItem;
    boolean isWebserviceRunning = false, isLoadmore = false, isFirstLoad = false;
    ProgressBar progressBar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(false);
        }

        init();
        setupDefaults();
        setupEvents();
    }

    private void init() {
        rvFeedList = (RecyclerView) findViewById(R.id.rv_feed_list);
        txtTryAgain = (TextView) findViewById(R.id.txt_try_again);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        fabMore = (FloatingActionButton) findViewById(R.id.fab_more);
        fabShare = (FloatingActionButton) findViewById(R.id.fab_share);
        fabRate = (FloatingActionButton) findViewById(R.id.fab_rate);
        fabMoreOpen = AnimationUtils.loadAnimation(this, R.anim.fab_open);
        fabMoreClose = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
//        RelativeLayout rlr_layout=(RelativeLayout) findViewById(R.id.main_content);
//        DeviceUtils.setSystemUiVisibility(rlr_layout);
        feedBeanList = new ArrayList<>();
    }

    private void setupDefaults() {
        progressDialog = new MTProgressDialog(this);
        progressDialog.setCancelable(false);

        rvFeedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (!isWebserviceRunning && feedBeanList.size() <= (lastVisibleItem + 5)) {
                    isLoadmore = true;
                    //count++;
                    progressBar.setVisibility(View.VISIBLE);
                    loadFlashNews();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });

        count = 0;
        isFirstLoad = true;
        loadFlashNews();

    }

    private void setupEvents() {
        fabMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFabOpen) {
                    fabMore.startAnimation(rotateBackward);
                    fabRate.startAnimation(fabMoreClose);
                    fabShare.startAnimation(fabMoreClose);
                    fabShare.setClickable(false);
                    fabRate.setClickable(false);
                    isFabOpen = false;
                } else {
                    fabMore.startAnimation(rotateForward);
                    fabRate.startAnimation(fabMoreOpen);
                    fabShare.startAnimation(fabMoreOpen);
                    fabShare.setClickable(true);
                    fabRate.setClickable(true);
                    isFabOpen = true;
                }
            }
        });



        fabRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
            }
        });

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                { Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, ""+getString(R.string.app_name));
                    String sAux = "\nLet me recommend you this application\n\n";
                    sAux = sAux + "https://play.google.com/store/apps/details?id="+ getPackageName()+" \n\n";
                    i.putExtra(Intent.EXTRA_TEXT, sAux);
                    startActivity(Intent.createChooser(i, "Share Via"));
                }
                catch(Exception e)
                { //e.toString();
                }
            }
        });

        rvFeedList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                closeFabButton();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }




    private void closeFabButton() {
        if (isFabOpen) {
            fabMore.startAnimation(rotateBackward);
            fabRate.startAnimation(fabMoreClose);
            fabShare.startAnimation(fabMoreClose);

            fabShare.setClickable(false);
            fabRate.setClickable(false);
            isFabOpen = false;
        }
    }


    private void loadFlashNews() {
        if (Utils.isConnected(this)) {
            if (asyncLatestNews != null) {
                asyncLatestNews.cancel(true);
                asyncLatestNews = null;
            }
            asyncLatestNews = new AsyncLatestNews();
            asyncLatestNews.execute();
        } else {
            Toast.makeText(HomeActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                menu.findItem(R.id.menu_night_mode_system).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_AUTO:
                menu.findItem(R.id.menu_night_mode_auto).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_YES:
                menu.findItem(R.id.menu_night_mode_night).setChecked(true);
                break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                menu.findItem(R.id.menu_night_mode_day).setChecked(true);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_night_mode_system:
                setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            case R.id.menu_night_mode_day:
                setNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.menu_night_mode_night:
                setNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.menu_night_mode_auto:
                setNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
                break;
            case R.id.ic_sync:
                count=0;
                isFirstLoad=true;
                isLoadmore=false;
                loadFlashNews();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNightMode(@AppCompatDelegate.NightMode int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        if (Build.VERSION.SDK_INT >= 11) {
            recreate();
        }
    }

    public class AsyncLatestNews extends AsyncTask<Void, Void, Void> {
        List<NewsItems> tmpList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tmpList = new ArrayList<>();
            if (!isLoadmore) {
                progressDialog = new MTProgressDialog(HomeActivity.this);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } else {
                isFirstLoad = false;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                isWebserviceRunning = true;
                Document document;
                // Connect to the web site
//                if(isFirstLoad) {
//                    document = Jsoup.connect(getResources().getString(R.string.base_url)).get();
//                    Log.e("Latest: ","True");
//                } else {
//                    document = Jsoup.connect(getResources().getString(R.string.base_url) + "category.php?id=1&page=" + count).get();
//                    Log.e("Latest: ","false");
//                }
                count++;
                document = Jsoup.connect(getResources().getString(R.string.base_url) + "category.php?id=1&page=" + count).timeout(60*1000).get();
                Elements div = document.select("div[class=widget_body]");
                for (Element element : div) {
                    Elements feedItems = element.select("div[class=feeditem]");
                    for (int i = 0; i < feedItems.size(); i++) {
                        NewsItems newsItems = new NewsItems();
                        newsItems.setNewsHeading("" + feedItems.get(i).getElementsByClass("item_url").text());
                        newsItems.setNewsUrl("" + feedItems.get(i).getElementsByTag("a").attr("href"));
                        Elements subDetails = feedItems.get(i).getElementsByClass("item_details");
                        for (int j = 0; j < subDetails.size(); j++) {
                            newsItems.setNewsDate("" + subDetails.get(j).getElementsByClass("item_datetime").text());
                            newsItems.setNewsPaper("" + subDetails.get(j).getElementsByTag("a").text());
                        }
                        tmpList.add(newsItems);
                    }

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (tmpList == null || tmpList.size() == 0) {
                if(!isLoadmore) {
                    count=0;
                    loadFlashNews();
                }
//                Toast.makeText(HomeActivity.this, "Loading Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
            Log.e("Total News Size: ", feedBeanList.size() + "");
            if (feedBeanList != null && feedBeanList.size() == 0) {
                feedBeanList.addAll(tmpList);
                rvFeedList.setHasFixedSize(true);
                rvFeedList.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                feedListAdapter = new NewsListAdapter(HomeActivity.this, feedBeanList);
                rvFeedList.setAdapter(feedListAdapter);
            } else if (!isLoadmore && isFirstLoad) {
                feedBeanList.clear();
                feedBeanList.addAll(tmpList);
                rvFeedList.setHasFixedSize(true);
                rvFeedList.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
                feedListAdapter = new NewsListAdapter(HomeActivity.this, feedBeanList);
                rvFeedList.setAdapter(feedListAdapter);
            } else {
                feedListAdapter.updateList(tmpList);
            }
            isLoadmore = false;
            isWebserviceRunning = false;
            progressBar.setVisibility(View.GONE);
//            DeviceUtils.setSystemUiVisibility(rvFeedList);
        }
    }



}

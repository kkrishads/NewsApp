package com.dreamworks.newsapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FlashNewsActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    List<NewsItems> newsList;
    RecyclerView newsListView;
    NewsListAdapter newsAdapter;
    AsyncLatestNews asyncLatestNews;
    ProgressBar progressBar;
    ProgressDialog dialog;
    int count = 1, lastVisibleItem;
    boolean isWebserviceRunning = false, isLoadmore = false, isFirstLoad = false;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flashnews_activity);
        newsListView = (RecyclerView) findViewById(R.id.list_headnews);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefersh);
        refreshLayout.setOnRefreshListener(this);
        progressBar.setVisibility(View.GONE);
        newsList = new ArrayList<>();
        refreshLayout.post(new Runnable() {
                               @Override
                               public void run() {
                                   count = 0;
                                   isFirstLoad = true;
                                   loadFlashNews();
                               }
                           }
        );
        newsListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                lastVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                if (!isWebserviceRunning && newsList.size() <= (lastVisibleItem + 5)) {
                    isLoadmore = true;
                    count++;
                    progressBar.setVisibility(View.VISIBLE);
                    loadFlashNews();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }
        });

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
            Toast.makeText(FlashNewsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }

    private void closeLoadings() {
        if (asyncLatestNews != null) {
            asyncLatestNews.cancel(true);
            asyncLatestNews = null;
        }
    }

    @Override
    public void onRefresh() {
        refreshLayout.setRefreshing(true);
        if (Utils.isConnected(this)) {
            count = 0;
            isFirstLoad = true;
            loadFlashNews();
        } else {
            Toast.makeText(FlashNewsActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
            refreshLayout.setRefreshing(false);
        }
    }


    public class AsyncLatestNews extends AsyncTask<Void, Void, Void> {
        List<NewsItems> tmpList;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tmpList = new ArrayList<>();
            if (!isLoadmore) {
                dialog = new ProgressDialog(FlashNewsActivity.this);
                dialog.setMessage("Tamil-Short News Loading...");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
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
                document = Jsoup.connect(getResources().getString(R.string.base_url) + "category.php?id=1&page=" + count).get();
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
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }

            if (tmpList == null || tmpList.size() == 0) {
                Toast.makeText(FlashNewsActivity.this, "Loading Failed Please Try Again", Toast.LENGTH_SHORT).show();
            }
            Log.e("Total News Size: ", newsList.size() + "");
            if (newsList != null && newsList.size() == 0) {
                newsList.addAll(tmpList);
                newsListView.setHasFixedSize(true);
                newsListView.setLayoutManager(new LinearLayoutManager(FlashNewsActivity.this));
                newsAdapter = new NewsListAdapter(FlashNewsActivity.this, newsList);
                newsListView.setAdapter(newsAdapter);
            } else if (!isLoadmore && isFirstLoad) {
                newsList.clear();
                newsList.addAll(tmpList);
                newsListView.setHasFixedSize(true);
                newsListView.setLayoutManager(new LinearLayoutManager(FlashNewsActivity.this));
                newsAdapter = new NewsListAdapter(FlashNewsActivity.this, newsList);
                newsListView.setAdapter(newsAdapter);
            } else {
                newsAdapter.updateList(tmpList);
            }
            isLoadmore = false;
            isWebserviceRunning = false;
            refreshLayout.setRefreshing(false);
            progressBar.setVisibility(View.GONE);
        }
    }

}

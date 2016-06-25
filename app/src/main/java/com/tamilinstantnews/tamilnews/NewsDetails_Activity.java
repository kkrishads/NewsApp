package com.tamilinstantnews.tamilnews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.tamilinstantnews.tamilnews.R;
import com.tamilinstantnews.tamilnews.utils.DeviceUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsDetails_Activity extends AppCompatActivity {

    private String newsDetails, newsImgUrl;
    private AsyncDetails asyncDetails;
    private TextView txtDetails, txtPublisher, txtDateTime, txtHeading;
    private ImageView imgNews;
    private String headings, dateTime, publisherName, url;
    private ProgressDialog dialog;
    private FloatingActionButton fabShare;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        if (getIntent().getStringExtra("HEADINGS") != null) {
            headings = getIntent().getStringExtra("HEADINGS");
            dateTime = getIntent().getStringExtra("DATE_TIME");
            publisherName = getIntent().getStringExtra("PUBLISHER");
            url = getIntent().getStringExtra("URL");
            setTitle(Html.fromHtml(headings));
        } else {
            finish();
        }
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        txtDateTime = (TextView) findViewById(R.id.txt_date_time);
        txtDetails = (TextView) findViewById(R.id.txt_news_details);
        txtPublisher = (TextView) findViewById(R.id.txt_publisher);
        txtHeading = (TextView) findViewById(R.id.txt_news_heading);
        imgNews = (ImageView) findViewById(R.id.img_news);
        fabShare=(FloatingActionButton) findViewById(R.id.fab_share);

        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = ""+txtDetails.getText().toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, ""+txtHeading.getText().toString());
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        loadDetails();
    }

    private void loadDetails() {
        if (Utils.isConnected(this)) {
            if (asyncDetails != null) {
                asyncDetails.cancel(true);
                asyncDetails = null;
            }
            asyncDetails = new AsyncDetails();
            asyncDetails.execute();
        } else {
            Toast.makeText(NewsDetails_Activity.this, "No Internet Conenction", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    class AsyncDetails extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(NewsDetails_Activity.this);
            dialog.setTitle("" + Html.fromHtml(headings));
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Connect to the web site
                Document document = Jsoup.connect(getResources().getString(R.string.base_url) + url).timeout(60*1000).get();
                Elements div = document.select("div[class=widget_body]");
                for (Element element : div) {
                    Elements feedItems = element.select("div[class=single_item_details]");
                    for (int i = 0; i < feedItems.size(); i++) {
                        newsDetails = feedItems.get(i).text();
                        newsImgUrl = feedItems.get(i).getElementsByTag("img").attr("src");
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
            if (newsDetails == null) {
                finish();
            } else {
                txtPublisher.setText("Source: " + Html.fromHtml(publisherName));
                txtDateTime.setText("" + Html.fromHtml(dateTime.replace("published on", "")));
                txtDetails.setText("" + Html.fromHtml(newsDetails.replace("View Photos", " ")));
                txtHeading.setText("" + Html.fromHtml(headings));
                if (newsImgUrl != null && !newsImgUrl.isEmpty()) {
                    Picasso.with(NewsDetails_Activity.this).load(newsImgUrl).placeholder(R.mipmap.ic_launcher).into(imgNews);
                }
            }

        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // handle arrow click here
//        if (item.getItemId() == android.R.id.home) {
//            finish(); // close this activity and return to preview activity (if there is any)
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_font_size, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.txt_size_large:
                setFontSize(getResources().getDimension(R.dimen.font_large));
                break;
            case R.id.txt_size_normal:
                setFontSize(getResources().getDimension(R.dimen.font_normal));
                break;
            case R.id.txt_size_small:
                setFontSize(getResources().getDimension(R.dimen.font_small));
                break;
            case android.R.id.home:
            finish(); // close this activity and return to preview activity (if there is any)
//        }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setFontSize(float dimension) {
        txtDetails.setTextSize(DeviceUtils.pixelsToSp(this, dimension));
        txtHeading.setTextSize(DeviceUtils.pixelsToSp(this, dimension));
    }
}

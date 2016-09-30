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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.squareup.picasso.Picasso;
import com.tamilinstantnews.tamilnews.R;
import com.tamilinstantnews.tamilnews.utils.DeviceUtils;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class NewsDetails_Activity extends AppCompatActivity implements InterstitialAdListener {

    private String newsDetails, newsImgUrl;
    private AsyncDetails asyncDetails;
    private TextView txtDetails, txtPublisher, txtDateTime, txtHeading;
    private ImageView imgNews;
    private String headings, dateTime, publisherName, url;
    private ProgressDialog dialog;
    private FloatingActionButton fabShare;
    private InterstitialAd interstitialAd;
    private AdView adView;
    private boolean adLoaded=false;
    final VunglePub vunglePub = VunglePub.getInstance();
    private void loadInterstitialAd(){
        adLoaded=false;
        interstitialAd = new InterstitialAd(this, "1559302264365604_1559305931031904");
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd();
    }
    @Override
    protected void onDestroy() {
        adView.destroy();
        super.onDestroy();
    }

    private void loadBannerAd(){
        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);

        final String app_id = "57ee0fea062879e91d00002c";

        // initialize the Publisher SDK
        vunglePub.init(this, app_id);

        vunglePub.setEventListeners(vungleDefaultListener, vungleSecondListener);

        adView = new AdView(this, "1559302264365604_1559305824365248", AdSize.BANNER_320_50);
//        AdSettings.addTestDevice("79890a473a7548850555943a5921cb5c");
        adViewContainer.addView(adView);
        adView.loadAd();

        adView.setAdListener(new AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad failed to load.
                // Add code to hide the ad's view
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad was loaded
                // Add code to show the ad's view
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Use this function to detect when an ad was clicked.
            }

        });
    }

    private final EventListener vungleDefaultListener = new EventListener() {
        @Deprecated
        @Override
        public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {
            // This method is deprecated and will be removed. Please use onAdEnd() instead.
        }

        @Override
        public void onAdStart() {
            // Called before playing an ad.
        }

        @Override
        public void onAdUnavailable(String reason) {
            // Called when VunglePub.playAd() was called but no ad is available to show to the user.
        }

        @Override
        public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {
            // Called when the user leaves the ad and control is returned to your application.
        }

        @Override
        public void onAdPlayableChanged(boolean isAdPlayable) {
            // Called when ad playability changes.
            Log.d("DefaultListener", "This is a default eventlistener.");
            final boolean enabled = isAdPlayable;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // Called when ad playability changes.
//                    setButtonState(buttonPlayAd, enabled);
//                    setButtonState(buttonPlayAdIncentivized, enabled);
//                    setButtonState(buttonPlayAdOptions, enabled);
                }
            });
        }
    };

    private final EventListener vungleSecondListener = new EventListener() {
        // Vungle SDK allows for multiple listeners to be attached. This secondary event listener is only
        // going to print some logs for now, but it could be used to Pause music, update a badge icon, etc.
        @Deprecated
        @Override
        public void onVideoView(boolean isCompletedView, int watchedMillis, int videoDurationMillis) {}

        @Override
        public void onAdStart() {}

        @Override
        public void onAdUnavailable(String reason) {}

        @Override
        public void onAdEnd(boolean wasSuccessfulView, boolean wasCallToActionClicked) {}

        @Override
        public void onAdPlayableChanged(boolean isAdPlayable) {
            Log.d("SecondListener", String.format("This is a second event listener! Ad playability has changed, and is now: %s", isAdPlayable));
        }
    };
    private void PlayAd() {
        vunglePub.playAd();
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        loadBannerAd();
        loadInterstitialAd();
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
                if(adLoaded)
                interstitialAd.show();
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

    @Override
    public void onInterstitialDisplayed(Ad ad) {

    }

    @Override
    public void onInterstitialDismissed(Ad ad) {

    }

    @Override
    public void onError(Ad ad, AdError adError) {

    }

    @Override
    public void onAdLoaded(Ad ad) {
        adLoaded=true;
    }

    @Override
    public void onAdClicked(Ad ad) {

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
                } else {
                    imgNews.setVisibility(View.GONE);
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

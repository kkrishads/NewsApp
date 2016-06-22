package com.dreamworks.newsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.List;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.CustomViewHolder> {
    List<NewsItems> newsItemses;
    Context context;

    public NewsListAdapter(Context context, List<NewsItems> newsItemses) {
        this.newsItemses = newsItemses;
        this.context = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemLayout = layoutInflater.inflate(R.layout.list_headnews_items, null);
        return new CustomViewHolder(itemLayout);

    }

    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        holder.txtHead.setText("" + Html.fromHtml(newsItemses.get(position).getNewsHeading()));
        holder.txtPublisher.setText("" + Html.fromHtml("Source: " + newsItemses.get(position).getNewsPaper().replace("latest","").replace("Latest","")));
        holder.txtDateTime.setText("" + Html.fromHtml(newsItemses.get(position).getNewsDate().replace("published on", "")));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetails_Activity.class);
                intent.putExtra("HEADINGS", newsItemses.get(position).getNewsHeading());
                intent.putExtra("DATE_TIME", newsItemses.get(position).getNewsDate());
                intent.putExtra("PUBLISHER", newsItemses.get(position).getNewsPaper());
                intent.putExtra("URL", newsItemses.get(position).getNewsUrl());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsItemses.size();
    }

    public void updateList(List<NewsItems> tmpList) {
        newsItemses.addAll(tmpList);
        notifyDataSetChanged();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView txtHead, txtDateTime, txtPublisher;
        LinearLayout linearLayout;
        public CustomViewHolder(View itemView) {
            super(itemView);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.lnr_item);
            txtHead = (TextView) itemView.findViewById(R.id.txt_headings);
            txtDateTime = (TextView) itemView.findViewById(R.id.txt_date_time);
            txtPublisher = (TextView) itemView.findViewById(R.id.txt_publisher);
        }
    }
}

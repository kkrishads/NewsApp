package com.tamilinstantnews.tamilnews.utils;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.pnikosis.materialishprogress.ProgressWheel;
import com.tamilinstantnews.tamilnews.R;

public class MTProgressDialog extends Dialog {
    private Context mContext;
    private int mProgressColorID;

    public MTProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        this.mContext = context;
        mProgressColorID = R.color.progress_color;
    }

    @Override
    public void show() {
        super.show();
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = layoutInflater.inflate(R.layout.view_progress_dialog, null, false);
        if (mProgressColorID != 0) {
            ProgressWheel progressWheel = (ProgressWheel) dialogView.findViewById(R.id.progress_wheel);
            progressWheel.setBarColor(ContextCompat.getColor(mContext, mProgressColorID));
        }
        setContentView(dialogView);
    }
}

package com.hollysmart.appupdatelibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import java.text.DecimalFormat;
public class DownloadDialog extends Dialog {
    private Context mContext;
    private TextView tv_progress;
    private ProgressBar progress;

    public DownloadDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.verson_update_progress_dialog, null);
        setContentView(mView);
        tv_progress = mView.findViewById(R.id.tv_progress);
        progress = mView.findViewById(R.id.progress);
        progress.setMax(100);
    }


    public void setProgress(long totalSize, long currentSize) {
        if (tv_progress == null || progress == null)
            return;
        String total = bToKbToMb(totalSize);
        String current = bToKbToMb(currentSize);
        tv_progress.setText(current + "/" + total);
        int pro = (int) (currentSize * 100 / totalSize );
        progress.setProgress(pro);
    }

    public String bToKbToMb(long b) {
        if (b >= 1024 * 1024) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(b / 1024f / 1024f) + "MB";
        } else if (b >= 1024) {
            DecimalFormat df = new DecimalFormat("#.00");
            return df.format(b / 1024f) + "KB";
        } else {
            return b + "B";
        }
    }
}


















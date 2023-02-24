package com.hollysmart.appupdatelibrary;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class VersionUpdateDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private OnClickOkListener onClickOkListener;
    private TextView tv_desc;
    private Button bn_update;
    private Button bn_cancel;

    private boolean mastUpdate;
    private String desc;


    public VersionUpdateDialog(@NonNull Context context, int themeResId, boolean mastUpdate, String desc) {
        super(context, themeResId);
        this.mContext = context;
        this.mastUpdate = mastUpdate;
        this.desc = desc;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View mView = LayoutInflater.from(mContext).inflate(R.layout.verson_update_dialog, null);
        setContentView(mView);

        tv_desc = mView.findViewById(R.id.tv_desc);
        bn_update = mView.findViewById(R.id.commit);
        bn_cancel = mView.findViewById(R.id.cancel);

        if (mastUpdate) {
            bn_cancel.setVisibility(View.GONE);
        }
        tv_desc.setText(TextUtils.isEmpty(desc) ? "" : desc);
        bn_update.setOnClickListener(this::onClick);
        bn_cancel.setOnClickListener(this::onClick);

    }

    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    @Override
    public void onClick(View v) {
        if (onClickOkListener != null) {
            int id = v.getId();
            if (id == R.id.cancel) {
                cancel();
            } else if (id == R.id.commit) {
                onClickOkListener.OnClickUpdate(v);
            }
        }
    }

    public interface OnClickOkListener {
        void OnClickUpdate(View view);
    }

}

package com.hollysmart.appupdate;

import android.os.Bundle;
import android.os.Environment;

import androidx.appcompat.app.AppCompatActivity;

import com.hollysmart.appupdate.ui.main.MainFragment;
import com.hollysmart.appupdatelibrary.AppUpdateUtil;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }
        String apkPath = Environment.getExternalStorageDirectory().getAbsolutePath() +
                File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator;
        String apkName = getString(R.string.app_name) + ".apk";
        AppUpdateUtil appUpdateUtil = new AppUpdateUtil(getApplication(), R.mipmap.ic_launcher,
                getString(R.string.app_name), apkPath, apkName);

        String downloadUrl = "http://aapk.hollysmart.com.cn/%E9%80%9A%E5%B7%9E12345/%E6%B5%8B%E8%AF%95%E7%8E%AF%E5%A2%83/tzapp-release-v1.0.5-5.apk";
        String remark = "测试版本更新";
        appUpdateUtil.checkUpdate(this, true, true, false,
                false, true, downloadUrl, remark);

    }
}
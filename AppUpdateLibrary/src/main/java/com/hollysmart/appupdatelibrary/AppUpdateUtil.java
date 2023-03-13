package com.hollysmart.appupdatelibrary;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.view.View;

import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.PermissionUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

public class AppUpdateUtil {

    private int icon;
    private String appName;
    private String apkPath;
    private String apkName;

    /**
     * 初始化
     * @param application
     * @param icon           应用图标
     * @param appName        应用名称
     * @param apkPath        apk下载地址
     * @param apkName        apk下载名称
     */
    public AppUpdateUtil(Application application,int icon, String appName, String apkPath, String apkName) {
        this.icon = icon;
        this.appName = appName;
        this.apkPath = apkPath;
        this.apkName = apkName;
        initOkGo(application);
    }


    /**
     * 柴   初始化 OKGO
     */
    public void initOkGo(Application application) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("com.http");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //全局的连接超时时间
        builder.connectTimeout(5, TimeUnit.SECONDS);
        //全局的读取超时时间
        builder.readTimeout(20, TimeUnit.SECONDS);
        //全局的写入超时时间
        builder.writeTimeout(20, TimeUnit.SECONDS);
        OkGo.getInstance().init(application)                           //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(2);                              //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                    //全局公共头
//                .addCommonParams(params);
    }

    /**
     * 检查更新
     *
     * @param activity
     * @param isMain
     * @param hasUpdate   是否有更新
     * @param mastUpdate  是否是必须更新
     * @param autoUpdate  是否是自动更新
     * @param needUpdate  是否需要在首页更新
     * @param downLoadURL apk下载地址
     * @param remark      更新说明
     */
    public void checkUpdate(Activity activity, boolean isMain, boolean hasUpdate, boolean mastUpdate,
                            boolean autoUpdate, boolean needUpdate, String downLoadURL, String remark) {
        if (isMain) {
            if (hasUpdate) {
                if (mastUpdate) {
                    Mlog.d("强制更新----");
                    updateApp(activity, mastUpdate, downLoadURL, remark);
                } else {
                    if (autoUpdate) {
                        Mlog.d("自动更新----");
                        autoDownloadApp(activity, downLoadURL);
                    } else {
                        if (needUpdate) {
                            Mlog.d("需要更新----");
                            updateApp(activity, mastUpdate, downLoadURL, remark);
                        }
                    }
                }
            }
        } else {
            if (hasUpdate) {
                updateApp(activity, mastUpdate, downLoadURL, remark);
            } else {
                ToastUtils.showShort("您已经是最新版本");
            }
        }
    }


    private void updateApp(Activity activity, final boolean mastUpdate, final String downLoadURL, String remark) {
        VersionUpdateDialog versionUpdateDialog = new VersionUpdateDialog(activity, R.style.cai_dialog_style, mastUpdate, remark);
        versionUpdateDialog.setCancelable(!mastUpdate); //设置不能取消
        versionUpdateDialog.setOnClickOkListener(new VersionUpdateDialog.OnClickOkListener() {
            @Override
            public void OnClickUpdate(View view) {
                PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .callback(new PermissionUtils.SimpleCallback() {
                            @Override
                            public void onGranted() {
                                downloadApp(activity, mastUpdate, downLoadURL);
                                versionUpdateDialog.cancel();
                            }

                            @Override
                            public void onDenied() {
                                ToastUtils.showShort("下载apk需要文件读写权限");
                            }
                        }).request();
            }
        });
        versionUpdateDialog.show();
    }

    private void downloadApp(Activity activity, final boolean mastUpdate, final String downLoadURL) {
        DownloadDialog downloadDialog = new DownloadDialog(activity, R.style.cai_dialog_style);
        downloadDialog.setCancelable(!mastUpdate);
        downloadDialog.show();
        OkGo.<File>get(downLoadURL)
                .execute(new FileCallback(apkPath, apkName) {
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Mlog.d("下载进度：" + progress.currentSize + "----总：" + progress.totalSize);
                        downloadDialog.setProgress(progress.totalSize, progress.currentSize);
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        downloadDialog.cancel();
                        Mlog.d("下载完成");
                        Mlog.d("文件位置：" + response.body().getAbsolutePath());
                        installApk(activity, apkPath + apkName, mastUpdate);
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                        downloadDialog.cancel();
                    }
                });
    }


    private void autoDownloadApp(Activity activity, final String downLoadURL) {
        createMessageNotificationChannel(activity);
        final int NEW_MESSAGE_ID = 0;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(activity, MESSAGES_CHANNEL);
        String title = "正在下载";
        String text = "我正在下载Apk文件";
        builder.setSmallIcon(icon) // //小图标
                .setContentTitle(title)  //通知标题
                .setContentText(text)  //描述性文本
                .setAutoCancel(false)    //点击通知后关闭通知
                .setOnlyAlertOnce(true); //设置提示音只响一次
        OkGo.<File>get(downLoadURL)
                .tag(this)
                .execute(new FileCallback(apkPath, apkName) {
                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                        Mlog.d("下载进度：" + progress.currentSize + "----总：" + progress.totalSize);
                        int pro = (int) (progress.currentSize * 100 / progress.totalSize);
                        builder.setProgress(100, pro, false);
                        builder.setContentText("下载" + pro + "%");
                        notificationManager.notify(NEW_MESSAGE_ID, builder.build());
                    }

                    @Override
                    public void onSuccess(Response<File> response) {
                        Mlog.d("下载完成");
                        Mlog.d("文件位置：" + response.body().getAbsolutePath());
                        installApk(activity, apkPath + apkName, false);
                        builder.setContentText("下载完成");
                        notificationManager.notify(NEW_MESSAGE_ID, builder.build());
                    }
                });
    }

    //定义notification实用的ID
    private static final String MESSAGES_CHANNEL = "messages";
    private NotificationManager notificationManager;

    //在Android8.0之后必须创建通知渠道
    private void createMessageNotificationChannel(Activity activity) {
        //Build.VERSION.SDK_INT 代表操作系统的版本号
        //Build.VERSION_CODES.O 版本号为26 对应的Android8.0版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    MESSAGES_CHANNEL,
                    appName,
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationManager = activity.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    /**
     * 安装APK
     *
     * @param activity    上下文
     * @param apkFilePath APK路径
     */
    private void installApk(Activity activity, String apkFilePath, boolean mastUpdate) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(apkFilePath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //这里要注意FileProvider一定要在AndroidManifest中去声明哦
            //同时com.mc.mcplatform代表你自己的包名
            String provider = activity.getPackageName() + ".appUpdateProvider";
            Uri apkUri = FileProvider.getUriForFile(activity,
                    provider, file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        activity.startActivity(intent);
        if (mastUpdate)
            activity.finish();
    }

}

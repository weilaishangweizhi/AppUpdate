# JsxTools

## Gradle引用

```
allprojects {
        repositories {
            ...
            maven { url 'https://jitpack.io' }
        }
    }
```

```
    implementation 'com.github.weilaishangweizhi:AppUpdate:Tag'//tag替换最新版本号
```

## 初始化

   需要配置FileProvider

```
<provider  
    android:name="com.hollysmart.appupdatelibrary.AppUpdateFileProvider"  
    android:authorities="${applicationId}.appUpdateProvider"  
    android:exported="false"  
    android:grantUriPermissions="true">  
    <meta-data        
        android:name="android.support.FILE_PROVIDER_PATHS"  
        android:resource="@xml/file_paths"/>  
</provider>
```

在res目录新建xml文件夹，创建file_paths.xml文件如下：

```
<?xml version="1.0" encoding="utf-8"?>
<paths>
    <external-path path="." name="files_root" />
</paths>
```

## 功能调用

#### 初始化

```
 /**
     * 初始化
     * @param application  
     * @param icon           应用图标
     * @param appName        应用名称
     * @param apkPath        apk下载地址
     * @param apkName        apk下载名称
     */
AppUpdateUtil appUpdateUtil = new AppUpdateUtil(getApplication(), 
 R.mipmap.ic_launcher, getString(R.string.app_name), apkPath, apkName);
```

#### 调用方法

```
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
     appUpdateUtil.checkUpdate(this, true, true, false,
                false, true, downloadUrl, remark);
```

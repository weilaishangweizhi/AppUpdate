package com.hollysmart.appupdate;

/**
 * {
 *         "appAddress":"{\"msg\":\"上传成功\",\"code\":200,\"data\":\"group1/M00/00/05/oYYBAGOuybKAYmdlBUe0lFzxYbk189.apk\"}",
 *         "appAddressTest":"https://ptech.bgosp.com/applicationGzt/getUrlAndSetAppCount?appId=f7b0844c6a1f444688957a248b286744&appEdition=v3.5.3&flag=1",
 *         "appAlias":"机关服务平台",
 *         "appClassify":"app_classify_04",
 *         "appEdition":"v3.5.3",
 *         "appEditionTest":"56",
 *         "appExplain":"解决了一些已知问题。",
 *         "appName":"机关服务平台APP",
 *         "appSize":"86509.14453125",
 *         "corpId":"",
 *         "dateCreated":"2022-12-30",
 *         "downloadNum":"20",
 *         "downloadUrl":"",
 *         "enable":"1",
 *         "forcedUpgrade":"2",
 *         "id":"f7b0844c6a1f444688957a248b286744",
 *         "lastUpdated":"2022-12-30",
 *         "logoFileName":"202212300719481608784915355467778.jpg",
 *         "logoUrl":"http://tech.jgj.egov.cn/file/staticFile/202212300719481608784915355467778.jpg",
 *         "plistAddress":"",
 *         "releaseTime":null,
 *         "sort":1
 *     }
 */
public class VersonInfo {
    private String appName;
    private String appAddressTest;
    private String appEdition;
    private int appEditionTest;
    private String appExplain;
    private String forcedUpgrade;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppAddressTest() {
        return appAddressTest;
    }

    public void setAppAddressTest(String appAddressTest) {
        this.appAddressTest = appAddressTest;
    }

    public String getAppEdition() {
        return appEdition;
    }

    public void setAppEdition(String appEdition) {
        this.appEdition = appEdition;
    }


    public int getAppEditionTest() {
        return appEditionTest;
    }

    public void setAppEditionTest(int appEditionTest) {
        this.appEditionTest = appEditionTest;
    }

    public String getAppExplain() {
        return appExplain;
    }

    public void setAppExplain(String appExplain) {
        this.appExplain = appExplain;
    }

    public String getForcedUpgrade() {
        return forcedUpgrade;
    }

    public void setForcedUpgrade(String forcedUpgrade) {
        this.forcedUpgrade = forcedUpgrade;
    }
}

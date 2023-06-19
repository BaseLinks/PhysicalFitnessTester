package com.kangear.bodycompositionanalyzer.entry;

public class PgyEntity {

    /**
     * code : 0
     * message :
     * data : {"buildBuildVersion":"3","forceUpdateVersion":"","forceUpdateVersionNo":"","needForceUpdate":false,"downloadURL":"https://www.pgyer.com/app/installUpdate/a421dd24af9a595087589024a780ffb0?sig=A0DfSXjrc2gTBpM5cdF6NUGSTbiys6VmD43vLrPuFfHQ145PMIbYUugAIBW7sxyr&forceHttps=","buildHaveNewVersion":true,"buildVersionNo":"2301","buildVersion":"v2.3.1","buildDescription":"测试升级","buildUpdateDescription":"测试升级","buildShortcutUrl":"https://www.pgyer.com/kUYC","appKey":"4473ae857b4e2de766983ae3c20baee1","buildKey":"a421dd24af9a595087589024a780ffb0","buildName":"人体成分分析仪","buildIcon":"https://cdn-app-icon.pgyer.com/5/c/f/6/5/5cf659e3b7bd7c06534aec4b84b34755?x-oss-process=image/resize,m_lfit,h_120,w_120/format,jpg","buildFileKey":"c2ab20bff9779a278ba9033b60a470e6.apk","buildFileSize":"49489736"}
     */

    private int code;
    private String message;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * buildBuildVersion : 3
         * forceUpdateVersion :
         * forceUpdateVersionNo :
         * needForceUpdate : false
         * downloadURL : https://www.pgyer.com/app/installUpdate/a421dd24af9a595087589024a780ffb0?sig=A0DfSXjrc2gTBpM5cdF6NUGSTbiys6VmD43vLrPuFfHQ145PMIbYUugAIBW7sxyr&forceHttps=
         * buildHaveNewVersion : true
         * buildVersionNo : 2301
         * buildVersion : v2.3.1
         * buildDescription : 测试升级
         * buildUpdateDescription : 测试升级
         * buildShortcutUrl : https://www.pgyer.com/kUYC
         * appKey : 4473ae857b4e2de766983ae3c20baee1
         * buildKey : a421dd24af9a595087589024a780ffb0
         * buildName : 人体成分分析仪
         * buildIcon : https://cdn-app-icon.pgyer.com/5/c/f/6/5/5cf659e3b7bd7c06534aec4b84b34755?x-oss-process=image/resize,m_lfit,h_120,w_120/format,jpg
         * buildFileKey : c2ab20bff9779a278ba9033b60a470e6.apk
         * buildFileSize : 49489736
         */

        private String buildBuildVersion;
        private String forceUpdateVersion;
        private String forceUpdateVersionNo;
        private boolean needForceUpdate;
        private String downloadURL;
        private boolean buildHaveNewVersion;
        private String buildVersionNo;
        private String buildVersion;
        private String buildDescription;
        private String buildUpdateDescription;
        private String buildShortcutUrl;
        private String appKey;
        private String buildKey;
        private String buildName;
        private String buildIcon;
        private String buildFileKey;
        private String buildFileSize;

        public String getBuildBuildVersion() {
            return buildBuildVersion;
        }

        public void setBuildBuildVersion(String buildBuildVersion) {
            this.buildBuildVersion = buildBuildVersion;
        }

        public String getForceUpdateVersion() {
            return forceUpdateVersion;
        }

        public void setForceUpdateVersion(String forceUpdateVersion) {
            this.forceUpdateVersion = forceUpdateVersion;
        }

        public String getForceUpdateVersionNo() {
            return forceUpdateVersionNo;
        }

        public void setForceUpdateVersionNo(String forceUpdateVersionNo) {
            this.forceUpdateVersionNo = forceUpdateVersionNo;
        }

        public boolean isNeedForceUpdate() {
            return needForceUpdate;
        }

        public void setNeedForceUpdate(boolean needForceUpdate) {
            this.needForceUpdate = needForceUpdate;
        }

        public String getDownloadURL() {
            return downloadURL;
        }

        public void setDownloadURL(String downloadURL) {
            this.downloadURL = downloadURL;
        }

        public boolean isBuildHaveNewVersion() {
            return buildHaveNewVersion;
        }

        public void setBuildHaveNewVersion(boolean buildHaveNewVersion) {
            this.buildHaveNewVersion = buildHaveNewVersion;
        }

        public String getBuildVersionNo() {
            return buildVersionNo;
        }

        public void setBuildVersionNo(String buildVersionNo) {
            this.buildVersionNo = buildVersionNo;
        }

        public String getBuildVersion() {
            return buildVersion;
        }

        public void setBuildVersion(String buildVersion) {
            this.buildVersion = buildVersion;
        }

        public String getBuildDescription() {
            return buildDescription;
        }

        public void setBuildDescription(String buildDescription) {
            this.buildDescription = buildDescription;
        }

        public String getBuildUpdateDescription() {
            return buildUpdateDescription;
        }

        public void setBuildUpdateDescription(String buildUpdateDescription) {
            this.buildUpdateDescription = buildUpdateDescription;
        }

        public String getBuildShortcutUrl() {
            return buildShortcutUrl;
        }

        public void setBuildShortcutUrl(String buildShortcutUrl) {
            this.buildShortcutUrl = buildShortcutUrl;
        }

        public String getAppKey() {
            return appKey;
        }

        public void setAppKey(String appKey) {
            this.appKey = appKey;
        }

        public String getBuildKey() {
            return buildKey;
        }

        public void setBuildKey(String buildKey) {
            this.buildKey = buildKey;
        }

        public String getBuildName() {
            return buildName;
        }

        public void setBuildName(String buildName) {
            this.buildName = buildName;
        }

        public String getBuildIcon() {
            return buildIcon;
        }

        public void setBuildIcon(String buildIcon) {
            this.buildIcon = buildIcon;
        }

        public String getBuildFileKey() {
            return buildFileKey;
        }

        public void setBuildFileKey(String buildFileKey) {
            this.buildFileKey = buildFileKey;
        }

        public String getBuildFileSize() {
            return buildFileSize;
        }

        public void setBuildFileSize(String buildFileSize) {
            this.buildFileSize = buildFileSize;
        }
    }
}

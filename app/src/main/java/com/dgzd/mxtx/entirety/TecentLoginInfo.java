package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <腾讯QQ登录>
 * @FileName: PersonalInfo.java
 * @author: Jessica
 */

public class TecentLoginInfo {

//    private String strScreenName;
    private String strPayToken;
    private String strAccessToken;
    private String strOpenId;
    private String strMsg;
    private String strProfileImageUrl;
    private int retId;

//    public void setScreenName(String strScreenName) {
//        this.strScreenName = strScreenName;
//    }
//
//    public String getScreenName() {
//        return strScreenName;
//    }

    public void setPayToken(String strPayToken) {
        this.strPayToken = strPayToken;
    }

    public String getPayToken() {
        return strPayToken;
    }

    public void setAccessToken(String strAccessToken) {
        this.strAccessToken = strAccessToken;
    }

    public String getAccessToken() {
        return strAccessToken;
    }

    public void setOpenId(String strOpenId) {
        this.strOpenId = strOpenId;
    }

    public String getOpenId() {
        return strOpenId;
    }

    public void setMsg(String strMsg) {
        this.strMsg = strMsg;
    }

    public String getMsg() {
        return strMsg;
    }

    public void setProfileImageUrl(String strProfileImageUrl) {
        this.strProfileImageUrl = strProfileImageUrl;
    }

    public String getProfileImageUrl() {
        return strProfileImageUrl;
    }

    public void setRetId(int retId) {
        this.retId = retId;
    }

    public int getRetId() {
        return retId;
    }
}

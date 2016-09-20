package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTripReplyInfo.java
 * @author: Jessica
 * @date: 2016-01-22 14:46
 */

public class MotoTripReplyInfo {
    private String strOfficialPhotoUrl;
    private String strOfficialReply;

    public MotoTripReplyInfo() {
        super();
    }

    public MotoTripReplyInfo(String strOfficialPhotoUrl, String strOfficalReply) {
        this.strOfficialPhotoUrl = strOfficialPhotoUrl;
        this.strOfficialReply = strOfficalReply;
    }
    public void setOfficialPhotoUrl(String strOfficialPhotoUrl) {
        this.strOfficialPhotoUrl = strOfficialPhotoUrl;
    }

    public String getOfficialPhotoUrl() {
        return strOfficialPhotoUrl;
    }

    public void setOfficialReply(String strOfficialReply) {
        this.strOfficialReply = strOfficialReply;
    }

    public String getOfficialReply() {
        return strOfficialReply;
    }
}

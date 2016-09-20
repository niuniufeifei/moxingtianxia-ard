package com.dgzd.mxtx.entirety;

import java.util.List;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTripRegisterInfo.java
 * @author: Jessica
 * @date: 2016-01-22 14:46
 */

public class MotoTripRegisterInfo {
    private String strPhotoUrl;
    private String strUserName;
    private String strUserComment;
    private List<MotoTripReplyInfo> replyList;
    private int UserId;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public MotoTripRegisterInfo() {
        super();
    }

    public MotoTripRegisterInfo(String strPhotoUrl, String strUserName, String strUserComment,
                                List<MotoTripReplyInfo> replyList) {
        this.strPhotoUrl = strPhotoUrl;
        this.strUserName = strUserName;
        this.strUserComment = strUserComment;
        this.replyList = replyList;
    }

    public void setUserName(String strUserName) {
        this.strUserName = strUserName;
    }

    public String getUserName() {
        return strUserName;
    }

    public void setPhotoUrl(String strPhotoUrl) {
        this.strPhotoUrl = strPhotoUrl;
    }

    public String getPhotoUrl() {
        return strPhotoUrl;
    }

    public void setUserComment(String strUserComment) {
        this.strUserComment = strUserComment;
    }

    public String getUserComment() {
        return strUserComment;
    }

    public void setOfficialReplyList(List<MotoTripReplyInfo> replyList) {
        this.replyList = replyList;
    }

    public List<MotoTripReplyInfo> getOfficialReplyList() {
        return replyList;
    }
}

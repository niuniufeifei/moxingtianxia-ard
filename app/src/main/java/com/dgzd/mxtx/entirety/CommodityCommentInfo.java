package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: CommodityCommentInfo.java
 * @author: Jessica
 * @date: 2016-01-22 14:46
 */

public class CommodityCommentInfo {
    private String strPhotoUrl;
    private String strPersonName;
    private String strUserComment;
    private int UserId;

    public String getStrPhotoUrl() {
        return strPhotoUrl;
    }

    public void setStrPhotoUrl(String strPhotoUrl) {
        this.strPhotoUrl = strPhotoUrl;
    }

    public String getStrPersonName() {
        return strPersonName;
    }

    public void setStrPersonName(String strPersonName) {
        this.strPersonName = strPersonName;
    }

    public String getStrUserComment() {
        return strUserComment;
    }

    public void setStrUserComment(String strUserComment) {
        this.strUserComment = strUserComment;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getStrCrateTime() {
        return strCrateTime;
    }

    public void setStrCrateTime(String strCrateTime) {
        this.strCrateTime = strCrateTime;
    }

    private String strCrateTime;

}

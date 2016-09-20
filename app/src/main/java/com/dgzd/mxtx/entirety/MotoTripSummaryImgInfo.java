package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTripSummaryImgInfo.java
 * @author: Jessica
 */

public class MotoTripSummaryImgInfo {
    private String strPhotoUrl;
    private boolean isVideo;

    public MotoTripSummaryImgInfo() {
        super();
    }

    public MotoTripSummaryImgInfo(String strPhotoUrl, boolean isVideo) {
        this.strPhotoUrl = strPhotoUrl;
        this.isVideo = isVideo;
    }


    public void setPhotoUrl(String strPhotoUrl) {
        this.strPhotoUrl = strPhotoUrl;
    }

    public String getPhotoUrl() {
        return strPhotoUrl;
    }

    public void setVideoFlag(boolean isVideo) {
        this.isVideo = isVideo;
    }

    public boolean isVideo() {
        return isVideo;
    }

}

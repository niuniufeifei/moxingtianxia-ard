package com.dgzd.mxtx.entirety;

/**
 * @version V1.0
 * @FileName: MyVideoInfo.java
 * @author: Jessica
 * @date: 2015-11-24 15:47
 */

public class MyVideoInfo {
    private int videoId;
    private int userId;
    private String imgUrl;
    private String strTitle;
    private String strTitle2;
    private String strDate;
    private String strVideoUrl;
    private boolean isSelected;

    public MyVideoInfo(){

    }

    public int getVideoId(){
        return videoId;
    }

    public void setVideoId(int videoId){
        this.videoId = videoId;
    }

    public int getUserId(){
        return userId;
    }

    public void setUserId(int userId){
        this.userId = userId;
    }

    public String getImageUrl(){
        return imgUrl;
    }

    public void setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

    public String getVideoUrl(){
        return strVideoUrl;
    }

    public void setVideoUrl(String strVideoUrl){
        this.strVideoUrl = strVideoUrl;
    }
    public String getTitle(){
        return strTitle;
    }

    public void setTitle(String strTitle){
       this.strTitle = strTitle;
    }

    public String getTitle2(){
        return strTitle2;
    }

    public void setTitle2(String strTitle){
        this.strTitle2 = strTitle;
    }

    public String getDate(){
        return strDate;
    }

    public void setDate(String strDate){
        this.strDate = strDate;
    }

    public Boolean getSelectStatus(){
        return isSelected;
    }

    public void setSelectStatus(boolean isSelected){
        this.isSelected = isSelected;
    }

}

package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: MotoTripInfo.java
 * @author: Jessica
 * @date: 2016-01-22 14:46
 */

public class MotoTripInfo {
    private int activityId;
    private float money;
    private String strImgUrl;
    private String strTitle;
    private String strUploadTime;
    private String strContactPerson;
    private String strContactMobile;
    private String strSummary;

    public MotoTripInfo() {
        super();
    }

    public MotoTripInfo(int activityId, String strImgUrl, String strTitle, String strUploadTime,
                        String strContactPerson, String strContactMobile, String strSummary) {
        this.activityId = activityId;
        this.strImgUrl = strImgUrl;
        this.strTitle = strTitle;
        this.strUploadTime = strUploadTime;
        this.strContactPerson = strContactPerson;
        this.strContactMobile = strContactMobile;
        this.strSummary = strSummary;
    }

    public void setActivityId(int activityId){
        this.activityId = activityId;
    }

    public int getActivityId(){
        return activityId;
    }

    public void setMoney(float money){
        this.money = money;
    }

    public float getMoney(){
        return money;
    }

    public void setImgUrl(String imgUrl) {
        this.strImgUrl = imgUrl;
    }

    public String getImgUrl() {
        return strImgUrl;
    }

    public void setTitle(String title) {
        this.strTitle = title;
    }

    public String getTitle() {
        return strTitle;
    }

    public void setUploadTime(String uploadTime) {
        this.strUploadTime = uploadTime;
    }

    public String getUploadTime() {
        return strUploadTime;
    }

    public void setContactPerson(String contactPerson) {
        this.strContactPerson = contactPerson;
    }

    public String getContactPerson() {
        return strContactPerson;
    }

    public void setContactMobile(String contactMobile) {
        this.strContactMobile = contactMobile;
    }

    public String getContactMobile() {
        return strContactMobile;
    }

    public void setSummary(String summary) {
        this.strSummary = summary;
    }

    public String getSummary() {
        return strSummary;
    }

}

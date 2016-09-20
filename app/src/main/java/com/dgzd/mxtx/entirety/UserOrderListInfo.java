package com.dgzd.mxtx.entirety;

/**
 * @version V1.0
 * @FileName: UserOrderListInfo.java
 * @author: Jessica
 */

public class UserOrderListInfo {
    private String imgUrl;
    private String strCommodityName;
    private int purchaserNum;
    private String strPrice;
    private int buyCount;

    public UserOrderListInfo() {

    }

    public UserOrderListInfo(String imgUrl, String strCommodityName, String strPrice, int purchaserNum) {
        this.imgUrl = imgUrl;
        this.strCommodityName = strCommodityName;
        this.strPrice = strPrice;
        this.purchaserNum = purchaserNum;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCommodityName() {
        return strCommodityName;
    }

    public void setCommodityName(String strCommodityName) {
        this.strCommodityName = strCommodityName;
    }

    public int getPurchaserNum() {
        return purchaserNum;
    }

    public void setPurchaserNum(int purchaserNum) {
        this.purchaserNum = purchaserNum;
    }

    public String getStrPrice() {
        return strPrice;
    }

    public void setStrPrice(String strPrice) {
        this.strPrice = strPrice;
    }

}

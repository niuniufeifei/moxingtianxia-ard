package com.dgzd.mxtx.entirety;

/**
 * @version V1.0
 * @FileName: ImageTextInfo.java
 * @author: Jessica
 * @date: 2015-11-24 15:47
 */

public class ImageFourTextInfo {
    public int pid;
    public String imgUrl;
    public String strCommodityName;
    public String strShopName;
    public String strPrice;
    public int purchaserNum;

    public ImageFourTextInfo(){

    }

    public ImageFourTextInfo(int pid, String imgUrl, String strCommodityName, String strShopName, String strPrice, int purchaserNum){
        this.pid = pid;
        this.imgUrl = imgUrl;
        this.strCommodityName = strCommodityName;
        this.strShopName = strShopName;
        this.strPrice = strPrice;
        this.purchaserNum = purchaserNum;
    }
}

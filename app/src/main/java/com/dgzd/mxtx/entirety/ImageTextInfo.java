package com.dgzd.mxtx.entirety;

/**
 * @version V1.0
 * @FileName: ImageTextInfo.java
 * @author: Jessica
 * @date: 2015-11-24 15:47
 */

public class ImageTextInfo {
    public String imgUrl;
    public String strText;
    public String strPrice;
    public boolean isShowPrice;

    public ImageTextInfo(){
    }
    public ImageTextInfo(String imgUrl, String strText, String strPrice, boolean isShowPrice){
        this.imgUrl = imgUrl;
        this.strText = strText;
        this.strPrice = strPrice;
        this.isShowPrice = isShowPrice;
    }
}

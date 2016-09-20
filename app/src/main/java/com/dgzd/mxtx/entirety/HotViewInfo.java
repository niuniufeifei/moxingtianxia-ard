package com.dgzd.mxtx.entirety;

import java.util.List;

/**
 * @version V1.0 <功能>
 * @FileName: HotViewInfo.java
 * @author: Jessica
 * @date: 2016-01-13 17:34
 */

public class HotViewInfo {

    private List<ImageTextInfo> infoList;
    private int imgResourceId;

    public void setInfoList(List<ImageTextInfo> infoList){
        this.infoList = infoList;
    }

    public List<ImageTextInfo> getInfoList(){
        return infoList;
    }

    public void setImgResourceId(int imgId){
        this.imgResourceId = imgId;
    }

    public int getImgResourceId(){
        return imgResourceId;
    }

}

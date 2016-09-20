package com.dgzd.mxtx.entirety;

import java.util.Collections;
import java.util.List;

/**
 * Created by nsd on 2016/3/4.
 * Notes：
 */
public class CarItemContent {
    private boolean Selected;
    SelectedOrderProduct OrderProductInfo;
    List<String> GiftList = Collections.EMPTY_LIST;

    public boolean isSelected() {
        return Selected;
    }

    public void setSelected(boolean selected) {
        Selected = selected;
    }

    public SelectedOrderProduct getOrderProductInfo() {
        return OrderProductInfo;
    }

    public void setOrderProductInfo(SelectedOrderProduct orderProductInfo) {
        OrderProductInfo = orderProductInfo;
    }

    public List<String> getGiftList() {
        return GiftList;
    }

    public void setGiftList(List<String> giftList) {
        GiftList = giftList;
    }
}

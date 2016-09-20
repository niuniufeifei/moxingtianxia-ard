package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: ShoppingCartItemListItemInfo.java
 * @author: Jessica
 */

public class ShoppingCartItemListItemInfo {
    private boolean selected;
    private ShoppingCartOrderProductInfo orderProductInfo;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ShoppingCartOrderProductInfo getOrderProductInfo() {
        return orderProductInfo;
    }

    public void setOrderProductInfo(ShoppingCartOrderProductInfo orderProductInfo) {
        this.orderProductInfo = orderProductInfo;
    }
}

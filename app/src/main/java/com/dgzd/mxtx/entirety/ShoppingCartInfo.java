package com.dgzd.mxtx.entirety;

import java.util.Collections;
import java.util.List;

/**
 * @version V1.0 <功能>
 * @FileName: ShoppingCartInfo.java
 * @author: Jessica
 */

public class ShoppingCartInfo {

    private int totalCount;
    private int productAmount;
    private int fullCut;
    private int orderAmount;
    private List<ShoppingCartItemInfo> shoppingCartItemInfoArray;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    public int getFullCut() {
        return fullCut;
    }

    public void setFullCut(int fullCut) {
        this.fullCut = fullCut;
    }

    public int getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(int orderAmount) {
        this.orderAmount = orderAmount;
    }

    public List<ShoppingCartItemInfo> getShoppingCartItemInfoArray() {
        return shoppingCartItemInfoArray;
    }

    public void setShoppingCartItemInfoArray(List<ShoppingCartItemInfo> shoppingCartItemInfoArray) {
        this.shoppingCartItemInfoArray = shoppingCartItemInfoArray;
    }
}

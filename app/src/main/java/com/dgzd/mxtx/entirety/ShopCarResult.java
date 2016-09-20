package com.dgzd.mxtx.entirety;

import java.util.Collections;
import java.util.List;

/**
 * Created by nsd on 2016/3/4.
 * Notes：
 */
public class ShopCarResult {

    private int TotalCount;
    private int FullCut;
    private double ProductAmount;
    private double OrderAmount;
    List<ShopCarItem> StoreCartList = Collections.EMPTY_LIST;

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public int getFullCut() {
        return FullCut;
    }

    public void setFullCut(int fullCut) {
        FullCut = fullCut;
    }

    public double getProductAmount() {
        return ProductAmount;
    }

    public void setProductAmount(double productAmount) {
        ProductAmount = productAmount;
    }

    public double getOrderAmount() {
        return OrderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        OrderAmount = orderAmount;
    }

    public List<ShopCarItem> getStoreCartList() {
        return StoreCartList;
    }

    public void setStoreCartList(List<ShopCarItem> storeCartList) {
        StoreCartList = storeCartList;
    }
}

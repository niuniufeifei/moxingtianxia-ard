package com.dgzd.mxtx.entirety;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version V1.0 <单个店铺>
 * @FileName: ShoppingCartItemInfo.java
 * @author: Jessica
 */

public class ShoppingCartItemInfo {
    private ShoppingCartStoreInfo storeInfo;
    public Map<Integer, Boolean> isCheckedMap = new HashMap<Integer, Boolean>();
    public boolean isChangedBySingleCheckbox = false;
    private List<ShoppingCartItemListItemInfo> itemListItemInfoArray;
    private List<ShoppingCartOrderProductInfo> selectedOrderProductArray;


    public Map<Integer, Boolean> getIsCheckedMap() {
        return isCheckedMap;
    }

    public void setIsCheckedMap(Map<Integer, Boolean> isCheckedMap) {
        this.isCheckedMap = isCheckedMap;
    }

    public boolean isChangedBySingleCheckbox() {
        return isChangedBySingleCheckbox;
    }

    public void setIsChangedBySingleCheckbox(boolean isChangedBySingleCheckbox) {
        this.isChangedBySingleCheckbox = isChangedBySingleCheckbox;
    }

    public List<ShoppingCartItemListItemInfo> getItemListItemInfoArray() {
        return itemListItemInfoArray;
    }

    public void setItemListItemInfoArray(List<ShoppingCartItemListItemInfo> itemListItemInfoArray) {
        this.itemListItemInfoArray = itemListItemInfoArray;
    }

    public ShoppingCartStoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(ShoppingCartStoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public List<ShoppingCartOrderProductInfo> getSelectedOrderProductArray() {
        return selectedOrderProductArray;
    }

    public void setSelectedOrderProductArray(List<ShoppingCartOrderProductInfo> selectedOrderProductArray) {
        this.selectedOrderProductArray = selectedOrderProductArray;
    }
}

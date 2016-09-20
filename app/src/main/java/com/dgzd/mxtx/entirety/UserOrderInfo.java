package com.dgzd.mxtx.entirety;

import java.util.List;

/**
 * @version V1.0
 * @FileName: UserOrderInfo.java
 * @author: Jessica
 */

public class UserOrderInfo {
    private String shopName;
    private int orderNumber;
    private int chatId;
    private int oid;
    private int storeId;
    private int payMoney;
    private int userid;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    private List<UserOrderListInfo> orderListArray;

    public UserOrderInfo() {

    }

    public UserOrderInfo(String shopName, int orderNumber, int chatId, List<UserOrderListInfo> orderListArray) {
        this.shopName = shopName;
        this.orderNumber = orderNumber;
        this.chatId = chatId;
        this.orderListArray = orderListArray;
    }

    public int getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(int payMoney) {
        this.payMoney = payMoney;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getChatId() {
        return chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public List<UserOrderListInfo> getOrderListArray() {
        return orderListArray;
    }

    public void setOrderListArray(List<UserOrderListInfo> orderListArray) {
        this.orderListArray = orderListArray;
    }

}

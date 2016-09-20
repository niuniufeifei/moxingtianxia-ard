package com.dgzd.mxtx.entirety;

import java.util.Collections;
import java.util.List;

/**
 * Created by nsd on 2016/3/4.
 * Notes：
 */
public class ShopCarItem {
    private String UserName;
    private int UserId;
    private StoreInfos StoreInfo;
    private List<SelectedOrderProduct> SelectedOrderProductList = Collections.EMPTY_LIST;
    private List<RemainedOrderProduct> RemainedOrderProductList = Collections.EMPTY_LIST;
    private List<CartItem> CartItemList = Collections.EMPTY_LIST;

 public String getUserName() {
  return UserName;
 }

 public void setUserName(String userName) {
  UserName = userName;
 }

 public int getUserId() {
  return UserId;
 }

 public void setUserId(int userId) {
  UserId = userId;
 }

 public StoreInfos getStoreInfo() {
  return StoreInfo;
 }

 public void setStoreInfo(StoreInfos storeInfo) {
  StoreInfo = storeInfo;
 }

 public List<SelectedOrderProduct> getSelectedOrderProductList() {
  return SelectedOrderProductList;
 }

 public void setSelectedOrderProductList(List<SelectedOrderProduct> selectedOrderProductList) {
  SelectedOrderProductList = selectedOrderProductList;
 }

 public List<RemainedOrderProduct> getRemainedOrderProductList() {
  return RemainedOrderProductList;
 }

 public void setRemainedOrderProductList(List<RemainedOrderProduct> remainedOrderProductList) {
  RemainedOrderProductList = remainedOrderProductList;
 }

 public List<CartItem> getCartItemList() {
  return CartItemList;
 }

 public void setCartItemList(List<CartItem> cartItemList) {
  CartItemList = cartItemList;
 }
}

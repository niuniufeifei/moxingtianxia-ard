package com.dgzd.mxtx.entirety;

/**
 * Created by nsd on 2016/3/4.
 * Notes：
 */
public class CartItem {
    private int Type;
    private CarItemContent Item;

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public CarItemContent getItem() {
        return Item;
    }

    public void setItem(CarItemContent item) {
        Item = item;
    }
}

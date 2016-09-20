package com.dgzd.mxtx.wheelWidget;

import com.dgzd.mxtx.entirety.AddressSelectInfo;

import java.util.List;

/**
 * WheelView 的适配器类
 * 
 * @param <T>
 *            元素类型
 */
public class ArrayWheelAdapter<T> implements WheelAdapter {

    /** 适配器的 元素集合(数据源) 默认长度为 -1 */
    public static final int DEFAULT_LENGTH = -1;

    /** 适配器的数据源 */
    private List<AddressSelectInfo> items;
    /** WheelView 的宽度 */
    private int length;

    /**
     * 构造方法
     * 
     * @param items
     *            适配器数据源 集合 T 类型的数组
     * @param length
     *            适配器数据源 集合 T 数组长度
     */
    public ArrayWheelAdapter(List<AddressSelectInfo> items, int length) {
        this.items = items;
        this.length = length;
    }

    /**
     * 构造方法
     * 
     * @param items
     *            适配器数据源集合 T 类型数组
     */
    public ArrayWheelAdapter(List<AddressSelectInfo> items) {
        this(items, DEFAULT_LENGTH);
    }

    
    @Override
    public String getItem(int index) {
    	//如果这个索引值合法, 就返回 item 数组对应的元素的字符串形式
        if (index >= 0 && index < items.size()) {
            return items.get(index).getName();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
    	//返回 item 数组的长度
        return items.size();
    }

    @Override
    public int getMaximumLength() {
    	//返回 item 元素的宽度
        return length;
    }

}

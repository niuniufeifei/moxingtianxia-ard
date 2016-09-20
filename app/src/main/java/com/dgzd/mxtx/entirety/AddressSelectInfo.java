package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: AddressSelectInfo.java
 * @author: Jessica
 * @date: 2016-02-16 18:02
 */

public class AddressSelectInfo {
    private String Name;
//    private String ProvinceName;
//    private String CityName;
//    private String AreaName;

    private int ProvinceId;
    private int CityId;
    private int RegionId;

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getName() {
        return Name;
    }


//    public void setProvinceName(String ProvinceName) {
//        this.ProvinceName = ProvinceName;
//    }
//
//    public String getProvinceName() {
//        return ProvinceName;
//    }
//
//    public void setCityName(String CityName) {
//        this.CityName = CityName;
//    }
//
//    public String getCityName() {
//        return CityName;
//    }
//
//    public void setAreaName(String AreaName) {
//        this.AreaName = AreaName;
//    }
//
//    public String getAreaName() {
//        return AreaName;
//    }

    public void setProvinceId(int ProvinceId) {
        this.ProvinceId = ProvinceId;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setCityId(int CityId) {
        this.CityId = CityId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setRegionId(int RegionId) {
        this.RegionId = RegionId;
    }

    public int getRegionId() {
        return RegionId;
    }
}

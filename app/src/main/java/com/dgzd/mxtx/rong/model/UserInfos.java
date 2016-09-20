package com.dgzd.mxtx.rong.model;

/**
 * Created by nsd on 2016/2/29.
 * Notes：
 */
public class UserInfos {
   /* "RongToken":null,
            "StoreName":null,
            "Address":null,
            "StoreLogo":null,
            "Regionid":0,
            "ProvinceId":0,
            "CityId":0,
            "Uid":1,
            "UserName":"mxtx",
            "Email":"z@163.com",
            "Mobile":"15555555555",
            "Password":"f5e5d1408fa9a7dd35723990536cdf92",
            "UserRid":8,
            "StoreId":2,
            "MallAGid":2,
            "NickName":"mxtx",
            "Avatar":"http://www.51mxtx.com/upload/user/1/ur_1512171802355030288.jpg",
            "PayCredits":1841,
            "RankCredits":638,
            "VerifyEmail":1,
            "VerifyMobile":1,
            "LiftBanTime":"1900-01-01 12:00:00",
            "Salt":"454698"}}*/
        private  String  Salt;
        private  String  LiftBanTime;
        private  String  Avatar;
        private  String  NickName;
        private  String  Password;
        private  String  Mobile;
        private  String  Email;
        private  String  UserName;
        private  String  StoreName;
        private  String  Address;
        private  String  StoreLogo;
        private  int  Regionid;
        private  int  ProvinceId;
        private  int  CityId;
        private  int  Uid;
        private  int  UserRid;
        private  int  StoreId;
        private  int  MallAGid;
        private  int  PayCredits;
        private  int  RankCredits;
        private  int  VerifyEmail;
        private  int  VerifyMobile;
        private  int  FriendStatus;

    public int getFriendStatus() {
        return FriendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        FriendStatus = friendStatus;
    }

    public String getSalt() {
        return Salt;
    }

    public void setSalt(String salt) {
        Salt = salt;
    }

    public String getLiftBanTime() {
        return LiftBanTime;
    }

    public void setLiftBanTime(String liftBanTime) {
        LiftBanTime = liftBanTime;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getStoreLogo() {
        return StoreLogo;
    }

    public void setStoreLogo(String storeLogo) {
        StoreLogo = storeLogo;
    }

    public int getRegionid() {
        return Regionid;
    }

    public void setRegionid(int regionid) {
        Regionid = regionid;
    }

    public int getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(int provinceId) {
        ProvinceId = provinceId;
    }

    public int getCityId() {
        return CityId;
    }

    public void setCityId(int cityId) {
        CityId = cityId;
    }

    public int getUid() {
        return Uid;
    }

    public void setUid(int uid) {
        Uid = uid;
    }

    public int getUserRid() {
        return UserRid;
    }

    public void setUserRid(int userRid) {
        UserRid = userRid;
    }

    public int getStoreId() {
        return StoreId;
    }

    public void setStoreId(int storeId) {
        StoreId = storeId;
    }

    public int getMallAGid() {
        return MallAGid;
    }

    public void setMallAGid(int mallAGid) {
        MallAGid = mallAGid;
    }

    public int getPayCredits() {
        return PayCredits;
    }

    public void setPayCredits(int payCredits) {
        PayCredits = payCredits;
    }

    public int getRankCredits() {
        return RankCredits;
    }

    public void setRankCredits(int rankCredits) {
        RankCredits = rankCredits;
    }

    public int getVerifyEmail() {
        return VerifyEmail;
    }

    public void setVerifyEmail(int verifyEmail) {
        VerifyEmail = verifyEmail;
    }

    public int getVerifyMobile() {
        return VerifyMobile;
    }

    public void setVerifyMobile(int verifyMobile) {
        VerifyMobile = verifyMobile;
    }
}

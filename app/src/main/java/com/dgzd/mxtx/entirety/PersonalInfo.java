package com.dgzd.mxtx.entirety;

/**
 * @version V1.0 <功能>
 * @FileName: PersonalInfo.java
 * @author: Jessica
 * @date: 2016-02-15 11:01
 */

public class PersonalInfo {

    /**
     * RongToken : null
     * StoreName : 布拉格跳广场舞的大妈
     * Address : 我们都市全pp
     * StoreLogo : http://192.168.0.115:8082/upload/store/3/logo/thumb100_100/BodyPart_04b29107-e723-45b0-8e25-765eda82f3a2.jpg
     * Regionid : 0
     * Uid : 2
     * UserName : brandon
     * Email : 172421772@qq.com
     * Mobile : 13963966979
     * Password : f5e5d1408fa9a7dd35723990536cdf92
     * UserRid : 7
     * StoreId : 3
     * MallAGid : 1
     * NickName : Hehe
     * Avatar : http://192.168.0.115:8082/upload/user/2/BodyPart_18d1be72-ffc0-4293-9965-570db62bfa6e.jpg
     * PayCredits : 40
     * RankCredits : 48
     * VerifyEmail : 1
     * VerifyMobile : 1
     * LiftBanTime : 1900-01-01 12:00:00
     * Salt : 454698
     */

    public TecentLoginInfo tecentLoginInfo;
    private Object RongToken;
    private String StoreName;
    private String Address;
    private String StoreLogo;
    private int Regionid;
    private int Uid = 0;
    private String UserName;
    private String Email;
    private String Mobile;
    private String Password;
    private int UserRid;
    private int StoreId;
    private int MallAGid;
    private String NickName;
    private String Avatar;
    private int PayCredits;
    private int RankCredits;
    private int VerifyEmail;
    private int VerifyMobile;
    private String LiftBanTime;
    private String Salt;

    public void setTecentLoginInfo(TecentLoginInfo tecentLoginInfo) {
        this.tecentLoginInfo = tecentLoginInfo;
    }

    public TecentLoginInfo getTecentLoginInfo() {
        return tecentLoginInfo;
    }

    public void setRongToken(Object RongToken) {
        this.RongToken = RongToken;
    }

    public void setStoreName(String StoreName) {
        this.StoreName = StoreName;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public void setStoreLogo(String StoreLogo) {
        this.StoreLogo = StoreLogo;
    }

    public void setRegionid(int Regionid) {
        this.Regionid = Regionid;
    }

    public void setUid(int Uid) {
        this.Uid = Uid;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public void setUserRid(int UserRid) {
        this.UserRid = UserRid;
    }

    public void setStoreId(int StoreId) {
        this.StoreId = StoreId;
    }

    public void setMallAGid(int MallAGid) {
        this.MallAGid = MallAGid;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public void setAvatar(String Avatar) {
        this.Avatar = Avatar;
    }

    public void setPayCredits(int PayCredits) {
        this.PayCredits = PayCredits;
    }

    public void setRankCredits(int RankCredits) {
        this.RankCredits = RankCredits;
    }

    public void setVerifyEmail(int VerifyEmail) {
        this.VerifyEmail = VerifyEmail;
    }

    public void setVerifyMobile(int VerifyMobile) {
        this.VerifyMobile = VerifyMobile;
    }

    public void setLiftBanTime(String LiftBanTime) {
        this.LiftBanTime = LiftBanTime;
    }

    public void setSalt(String Salt) {
        this.Salt = Salt;
    }

    public Object getRongToken() {
        return RongToken;
    }

    public String getStoreName() {
        return StoreName;
    }

    public String getAddress() {
        return Address;
    }

    public String getStoreLogo() {
        return StoreLogo;
    }

    public int getRegionid() {
        return Regionid;
    }

    public int getUid() {
        return Uid;
    }

    public String getUserName() {
        return UserName;
    }

    public String getEmail() {
        return Email;
    }

    public String getMobile() {
        return Mobile;
    }

    public String getPassword() {
        return Password;
    }

    public int getUserRid() {
        return UserRid;
    }

    public int getStoreId() {
        return StoreId;
    }

    public int getMallAGid() {
        return MallAGid;
    }

    public String getNickName() {
        return NickName;
    }

    public String getAvatar() {
        return Avatar;
    }

    public int getPayCredits() {
        return PayCredits;
    }

    public int getRankCredits() {
        return RankCredits;
    }

    public int getVerifyEmail() {
        return VerifyEmail;
    }

    public int getVerifyMobile() {
        return VerifyMobile;
    }

    public String getLiftBanTime() {
        return LiftBanTime;
    }

    public String getSalt() {
        return Salt;
    }
}

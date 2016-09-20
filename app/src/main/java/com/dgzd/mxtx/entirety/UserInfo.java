package com.dgzd.mxtx.entirety;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * 类名称：UserInfo
 * 创建人：niusd
 */
public class UserInfo {
	/**
	 * 用户ID
	 */
	private String userId;
	/**
	 * 用户姓名
	 */
	private String userName;
	/**
	 * 用户登录名
	 */
	private String userLoginname;
	/**
	 * 用户登录密码
	 */
	private String userPassword;
	/**
	 * 
	 */
	private String enterId;
	/**
	 * 
	 */
	private String enterName;
	/**
	 * 
	 */
	private String deptId;
	/**
	 * 
	 */
	private String deptName;
	/**
	 * 
	 */
	private String userLevel;
	/**
	 * 
	 */
	private String userType;
	/**
	 * 
	 */
	private String userPhone;
	/**
	 * 
	 */
	private String userMobile;
	/**
	 * 
	 */
	private String userEmail;
	/**
	 * 
	 */
	private String userFax;
	/**
	 * 
	 */
	private String userGender;
	/**
	 * 
	 */
	private String isOnline;
	/**
	 * 
	 */
	private Date passupdateTime;
	/**
	 * 
	 */
	private Date lastvisitTime;
	/**
	 * 
	 */
	private BigDecimal loginCount;
	/**
	 * 
	 */
	private Date addTime;
	/**
	 * 
	 */
	private String remark;
	/**
	 * 
	 */
	private String spare1;
	/**
	 * 
	 */
	private String spare2;
	/**
	 * 
	 */
	private String spare3;
	/**
	 * 
	 */
	private String spare4;
	/**
	 * 
	 */
	private String userCode;
	/**
	 * 
	 */
	private String treatEnterId;
	/**
	 * 
	 */
	private String treatEnterName;
	/**
	 * 
	 */
	private String ifEffect;
	/**
	 * 
	 */
	private  Date effectTime;
	public String getIfEffect() {
		return ifEffect;
	}

	public void setIfEffect(String ifEffect) {
		this.ifEffect = ifEffect;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	/**
	 * 
	 * @return
	 *//*
	private List<String> funcList;
	private List<String> roleList;*/

	public String getUserLoginname() {
		return userLoginname;
	}

	public void setUserLoginname(String userLoginname) {
		this.userLoginname = userLoginname;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getEnterId() {
		return enterId;
	}

	public void setEnterId(String enterId) {
		this.enterId = enterId;
	}

	public String getEnterName() {
		return enterName;
	}

	public void setEnterName(String enterName) {
		this.enterName = enterName;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserFax() {
		return userFax;
	}

	public void setUserFax(String userFax) {
		this.userFax = userFax;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getIsOnline() {
		return isOnline;
	}

	public void setIsOnline(String isOnline) {
		this.isOnline = isOnline;
	}

	public Date getPassupdateTime() {
		return passupdateTime;
	}

	public void setPassupdateTime(Date passupdateTime) {
		this.passupdateTime = passupdateTime;
	}

	public Date getLastvisitTime() {
		return lastvisitTime;
	}

	public void setLastvisitTime(Date lastvisitTime) {
		this.lastvisitTime = lastvisitTime;
	}

	public BigDecimal getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(BigDecimal loginCount) {
		this.loginCount = loginCount;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSpare1() {
		return spare1;
	}

	public void setSpare1(String spare1) {
		this.spare1 = spare1;
	}

	public String getSpare2() {
		return spare2;
	}

	public void setSpare2(String spare2) {
		this.spare2 = spare2;
	}

	public String getSpare3() {
		return spare3;
	}

	public void setSpare3(String spare3) {
		this.spare3 = spare3;
	}

	public String getSpare4() {
		return spare4;
	}

	public void setSpare4(String spare4) {
		this.spare4 = spare4;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

	public String getTreatEnterId() {
		return treatEnterId;
	}

	public void setTreatEnterId(String treatEnterId) {
		this.treatEnterId = treatEnterId;
	}

	public String getTreatEnterName() {
		return treatEnterName;
	}

	public void setTreatEnterName(String treatEnterName) {
		this.treatEnterName = treatEnterName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}

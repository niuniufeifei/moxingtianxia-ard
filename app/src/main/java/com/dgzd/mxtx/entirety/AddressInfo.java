package com.dgzd.mxtx.entirety;

public class AddressInfo {
	/**
	 * address
	 */
	private String strAddress;
	/**
	 *consignee
	 */
	private String strConsignee;
	/**
	 * contact way
	 */
	private String strContactWay;
	/**
	 * post code
	 */
	private String strPostCode;
	/**
	 * shopping address
	 */
	private String strShoppingAddress;

	public String getAddress() {
		return strAddress;
	}

	public void setAddress(String strAddress) {
		this.strAddress = strAddress;
	}

	public String getConsignee() {
		return strConsignee;
	}

	public void seConsignee(String strConsignee) {
		this.strConsignee = strConsignee;
	}

	public String getContactWay() {
		return strContactWay;
	}

	public void setContactWay(String strContactWay) {
		this.strContactWay = strContactWay;
	}

	public String getPostCode() {
		return strPostCode;
	}

	public void setPostCode(String strPostCode) {
		this.strPostCode = strPostCode;
	}

	public String getShoppingAddress() {
		return strShoppingAddress;
	}

	public void setShoppingAddress(String strShoppingAddress) {
		this.strShoppingAddress = strShoppingAddress;
	}
}

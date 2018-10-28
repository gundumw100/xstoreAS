package com.base.net;

import java.io.Serializable;

public class AccessToken implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -613276478658820288L;
	String appId;
	String createDate;
    long expirySeconds;
    String secretKey;
    String token;
    String userCode;
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    	return appId+";"+token+";"+userCode;
    }
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public long getExpirySeconds() {
		return expirySeconds;
	}
	public void setExpirySeconds(long expirySeconds) {
		this.expirySeconds = expirySeconds;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserCode() {
		return userCode;
	}
	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}
    	
}

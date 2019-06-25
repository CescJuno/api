package com.skt.api.user.vo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class User {
	
	String userNo;
	@NotNull
	@Size(min=1)
	String userId;
	String userPwd;
	String ctn;
	String createDate;
	String updateDate;
	
	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCtn() {
		return ctn;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public void setCtn(String ctn) {
		this.ctn = ctn;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}

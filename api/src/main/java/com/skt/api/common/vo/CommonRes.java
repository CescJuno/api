package com.skt.api.common.vo;


public class CommonRes extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -920730632004792933L;
	private Common common;
	public Common getCommon() {
		return common;
	}
	
	public void setCommon(Common common) {
		this.common = common;
	}
	
	public CommonRes(Common common) {
		this.common = common;
	}
	
	public static class Common extends BaseModel {
		private String resultCode;
		private String resultMsg;
		
		public String getResultCode() {
			return resultCode;
		}
		
		public String getResultMsg() {
			return resultMsg;
		}
		
		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}
		
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
	}
}

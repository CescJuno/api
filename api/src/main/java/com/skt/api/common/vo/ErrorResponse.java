package com.skt.api.common.vo;

public class ErrorResponse extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8070913749533101832L;
	private Common common;

	public ErrorResponse(Common common2) {
		// TODO Auto-generated constructor stub
		this.common = common2;
	}

	/**
	 * @return the common
	 */
	public Common getCommon() {
		return common;
	}

	/**
	 * @param common the common to set
	 */
	public void setCommon(Common common) {
		this.common = common;
	}
	
	public static class Common extends BaseModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -862560387250542985L;
		private String resultCode;
		private String resultMsg;
		public String getResultCode() {
			return resultCode;
		}
		public void setResultCode(String resultCode) {
			this.resultCode = resultCode;
		}
		public String getResultMsg() {
			return resultMsg;
		}
		public void setResultMsg(String resultMsg) {
			this.resultMsg = resultMsg;
		}
		
		
	}
}

package com.skt.api.common.resource;

public enum CodeConstants {
	value, name;
	
	public enum RESULT_MAP {
		value;
		public final static String CODE = "RESULT_CODE";
		public final static String MSG = "RESULT_MSG";
		public final static String RESULT = "RESULT";
	}
	
	/*
	public enum RESULT_CODE {
		value;
		public final static String SUCCESS 					= "20000000";
		public final static String ERROR_HTTP_METHOD 		= "30000000";
		public final static String ERROR_HTTP_PARSING		= "30000001";
		public final static String ERROR_VALID_PARAM 		= "30000003";
		public final static String ERROR_HTTP_MULTIPART 	= "30000004";
		public final static String ERROR_NOT_FOUND_URL		= "30000006";
		public final static String ERROR_DUPLICATION_EMAL	= "30000007";
	}
	*/
	
	public enum RESULT_CODE {
		API2000("성공"),
		//POC앱 요청 오류
		API3000("잘못된 접근 - 적절하지 않은 http method"),
		API3001("파라미터 규격 오류(파싱 불가)"),
		API3006("잘못된 접근 – 유효하지 않은 URL"),
		//OCR 서버 오류
		API4003("데이터베이스 처리 오류"),
		//OCR4024("인증 실패(옳지 않은 인증 방식)"),	//deprecated
		API4999("기타 알수 없는 오류")
		;
		

		public final static String SUCCESS 					= "20000000";
		public final static String ERROR_HTTP_METHOD 		= "30000000";
		public final static String ERROR_HTTP_PARSING		= "30000001";
		public final static String ERROR_VALID_PARAM 		= "30000003";
		public final static String ERROR_HTTP_MULTIPART 	= "30000004";
		public final static String ERROR_NOT_FOUND_URL		= "30000006";
		public final static String ERROR_DUPLICATION_EMAL	= "30000007";
		private final String value;
		RESULT_CODE(String value) { this.value = value; }
		public String getValue() { return value; }
	}
	public enum RESULT_MSG {
		value;
		public final static String SUCCESS 					= "성공";
		public final static String ERROR_HTTP_METHOD 		= "잘못된접근 - 적절하지 않은 http method";
		public final static String ERROR_HTTP_PARSING		= "파라미터 규격 오류(파싱 불가)";
		public final static String ERROR_VALID_PARAM 		= "파라미터 Validation 오류";
		public final static String ERROR_HTTP_MULTIPART	 	= "잘못된접근 - 지원하지 않는 Http MediaType";
		public final static String ERROR_NOT_FOUND_URL		= "잘못된접근 - 유효하지 않는 URL";
		public final static String ERROR_DUPLICATION_EMAL	= "이메일 중복";
		
	}
	public enum ADMIN_CODE {
		value;
		public final static String ERROR_ADMIN_DUPLICATE 	= "1001";		// 중복 사용자 등록
		public final static String ERROR_ADMIN_VALIDATION 	= "1002";		// Validation Error
		public final static String ERROR_ADMIN_CNT 			= "1003";		// 로그인 실패 횟수 초과
		public final static String ERROR_ADMIN_PWD 			= "1004";		// 비밀번호 오류
		public final static String ERROR_ADMIN_EXPIRED		= "1005";		// 비밀번호 90일 만료
		public final static String ERROR_ADMIN_EMPTY		= "1005";		// 회원정보없음
	}

	public enum ADMIN_MSG {
		value;
		public final static String ERROR_ADMIN_DUPLICATE 	= "이미 중복된 사용자입니다.";		// 중복 사용자 등록
		public final static String ERROR_ADMIN_CNT 			= "로그인 실패 횟수 초과";			// 로그인 실패 횟수 초과
		public final static String ERROR_ADMIN_PWD 			= "비밀번호 오류";				// 비밀번호 오류
		public final static String ERROR_ADMIN_EXPIRED		= "비밀번호 90일 만료";			// 비밀번호 90일 만료
		public final static String ERROR_ADMIN_EMPTY		= "회원정보없음";				// 회원정보없음
		
	}
	
	public static final String CLIENT = "my-trusted-client";
	public static final String RESOURCE = "ocr_rest_api";
	public static final String REALM = "OCR_OAUTH_REALM";
	
	public enum AUTH_CODE {
		INVAID_BASIC(new String[]{"invalid_basic","Invalid basic authorization code"}),
		INVAID_REQUEST(new String[]{"invalid_request","Missing grant type"}),
		INVAID_GRANT(new String[]{"invalid_grant","Bad credentials"}),
		
		INVAID_ACCESS(new String[]{"invalid_token","Invalid access token"}),
		INVAID_REFRESH(new String[]{"invalid_token","Invalid refresh token"}),
		
		EXPIRED_ACCESS(new String[]{"invalid_token","Access token expired"}),
		EXPIRED_REFRESH(new String[]{"invalid_token","Refresh token expired"}),
		
		SERVER_ERROR(new String[]{"server_error","server error"})
		;
		private final String[] value;
		AUTH_CODE(String[] value) { this.value = value; }
		public String[] getValue() { return value; }
	}
	
	public enum GRANT_TYPE {
		PASSWORD("password"),
		REFRESH_TOKEN("refresh_token")
		;
		private final String value;
		GRANT_TYPE(String value) { this.value = value; }
		public String getValue() { return value; }
	}
	
	
}

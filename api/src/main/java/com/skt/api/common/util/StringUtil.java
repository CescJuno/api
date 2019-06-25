package com.skt.api.common.util;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;

public class StringUtil {
	
	public static Map<String, Object> parse(String json) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtil.isEmptyStr(json))
			return null;

		ObjectMapper mapper = new ObjectMapper();
		MapType mapType = mapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
		Map<String, Object> result = mapper.readValue(json, mapType);

		return result;
	}

	/**
	 * int 타입의 숫자를 지정된 자리수의 문자로 만든다.<br>
	 * 예) makeFixedSizeInt(100, 5) 는 "00100"를 리턴한다.<br>
	 * 
	 * @param strValue
	 *            변환 대상이 되는 숫자 (String type)
	 * @param nSize
	 *            변환후의 길이, x 자리
	 * @return 즉, 이 함수는 앞에 0 을 맞추어 넣어 준다. 예) makeFixedSizeInt(100, 5) 는 "00100"를
	 *         리턴 만약 변환 대상 숫자의 길이가 만들기를 원하는 지정 길이보다 이미 클 경우에는 변환 대상 숫자를 문자열로만
	 *         바꾸어 리턴한다.
	 */
	public static String makeFixedSizeIntAuto(int seq, int nSize) {
		String strValue = Integer.toString(seq);

		int nCurSize = strValue.length();
		if (nCurSize >= nSize){
			return strValue;
		}
		
		int diff = nSize - nCurSize;
		StringBuilder sb = new StringBuilder(nSize);

		for (int i = 0; i < diff; i++){
			sb.append("0");
		}
		
		sb.append(strValue);

		String rtn = sb.toString().trim();
		sb.setLength(0);
		sb = null;
		seq++;
		if(seq==10000){
			seq=0;
		}
		return rtn;
	}
	/**
	 * int 타입의 숫자를 지정된 자리수의 문자로 만든다.<br>
	 * 예) makeFixedSizeInt(100, 5) 는 "00100"를 리턴한다.<br>
	 * 
	 * @param strValue
	 *            변환 대상이 되는 숫자 (String type)
	 * @param nSize
	 *            변환후의 길이, x 자리
	 * @return 즉, 이 함수는 앞에 0 을 맞추어 넣어 준다. 예) makeFixedSizeInt(100, 5) 는 "00100"를
	 *         리턴 만약 변환 대상 숫자의 길이가 만들기를 원하는 지정 길이보다 이미 클 경우에는 변환 대상 숫자를 문자열로만
	 *         바꾸어 리턴한다.
	 */
	public static String makeFixedSizeStringAuto(String strValue, int nSize) {
	
		int nCurSize = strValue.length();
		if (nCurSize >= nSize){
			return strValue;
		}
		
		int diff = nSize - nCurSize;
		char Astring =65; //'A'
		StringBuilder sb = new StringBuilder(nSize);
		sb.append(strValue);
		
		for (int i = 0; i < diff; i++){
			if(i%26==0){
				Astring =65;
			}
			sb.append(String.valueOf(Astring++));
			
		}
		String rtn = sb.toString().trim();
		sb.setLength(0);
		sb = null;
		return rtn;
	}
	/**
	 * int 타입의 숫자를 지정된 자리수의 문자로 만든다.<br>
	 * 예) makeFixedSizeInt(100, 5) 는 "00100"를 리턴한다.<br>
	 * 
	 * @param strValue
	 *            변환 대상이 되는 숫자 (String type)
	 * @param nSize
	 *            변환후의 길이, x 자리
	 * @return 즉, 이 함수는 앞에 0 을 맞추어 넣어 준다. 예) makeFixedSizeInt(100, 5) 는 "00100"를
	 *         리턴 만약 변환 대상 숫자의 길이가 만들기를 원하는 지정 길이보다 이미 클 경우에는 변환 대상 숫자를 문자열로만
	 *         바꾸어 리턴한다.
	 */
	public static int intIncreaser(int seq, int nSize){
		seq++;
		if(seq == (10^nSize) ){
			seq = 0; 
		}
		return seq;
	}
	
	/**
	 * string 타입의 숫자를 int로 만든다.<br>
	 * 값이 null 혹은 공백인 경우 0을 리턴한다.
	 */
	public static int getParamInt(String str) {
		if ((str == null) || str.trim().length() < 1) {
			return 0;
		}
		else {
			return Integer.parseInt(str.replace(",", ""));
		}
	}
	
	public static String toString(Object obj) {
		return obj==null? "":obj.toString();
	}
	
	public static boolean isEmptyStr(Object p) {
		return (p == null || p.toString().trim().equals(""));
	}
	
	public static String createTemporaryPassword(int maxLength) {
		String password = "";
		for(int i = 0; i < maxLength; i++) {
			char lowerStr = (char) (Math.random() *26+97);
			if(i % 2 == 0) {
				password +=(int)(Math.random() * 10);
			}else {
				password += lowerStr;
			}
			
		}
		return password;
	}
	
	public static String makePhoneNumber(String phoneNumber, int type) {
		String regEx = "(\\d{3})(\\d{3,4})(\\d{4})";
		String str = "$1-$2-$3";
		if(phoneNumber.length() == 11) {
			regEx = "(\\d{3})(\\d{4})(\\d{4})";
			if(type==0) str = "$1-****-$3";
		}
		else if(phoneNumber.length() == 8) {
			regEx = "(\\d{4})(\\d{4})";
			if(type==0) str = "$1-$2";
		}else {
			if(phoneNumber.indexOf("02")==0) {
				if(phoneNumber.length() == 9) {
					regEx = "(\\d{2})(\\d{3})(\\d{4})";
					if(type==0) str = "$1-***-$3";
				}else {
					regEx = "(\\d{2})(\\d{4})(\\d{4})";
					if(type==0) str = "$1-****-$3";
				}
			}else {
				if(phoneNumber.length() == 10) {
					regEx = "(\\d{3})(\\d{3})(\\d{4})";
					if(type==0) str = "$1-***-$3";
				}else {
					regEx = "(\\d{3})(\\d{4})(\\d{4})";
					if(type==0) str = "$1-****-$3";
				}
			}
		}
		
		if(!Pattern.matches(regEx, phoneNumber)) {
			if(type == 0) {
				String asterisk = "";
				int length = phoneNumber.length();
				for(int i=0;i<length-2;i++) {
					asterisk+="*";
				}
				return phoneNumber.substring(0, 1)+asterisk;
			}else {
				return phoneNumber;
			}
			
		}
		
		return phoneNumber.replaceAll(regEx, str);
	}
	public static String makeMaskedName(String name) {
		if(name == null) return name;
		else {
			if(name.equals("")) return name;
		}
		
		String asterisk = "";
		int len = name.length()-2;
		
		if(len == 0) len = 1;
		
		for(int i=0; i< len; i++) {
			asterisk = asterisk+"*";
		}
		String rdata = name.substring(0, 1)+asterisk;
		String rdata2 = name.substring(len+1, name.length());
		return rdata+rdata2;
	}
	
	public static String makeMaskedEmail(String email) {
		if(email == null) return email;
		else {
			if(email.equals("")) return email;
		}
		
		String regex = "\\b(\\S+)+@(\\S+.\\S+)";
		Matcher matcher = Pattern.compile(regex).matcher(email);
		
		if(matcher.find()) {
			String id = matcher.group(1);
			
			int length = id.length();
			if(length < 3) {
				char[] c = new char[length];
				Arrays.fill(c, '*');
				return email.replace(id, String.valueOf(c));
			}else if(length == 3) {
				return email.replaceAll("\\b(\\S+)[^@][^@]+@(\\S+)", "$1**@$2");
			}else {
				return email.replaceAll("\\b(\\S+)[^@][^@][^@]+@(\\S+)", "$1***@$2");
			}
		}else {
			String asterisk = "";
			int length = email.length();
			for(int i=1; i< length-3; i++) {
				asterisk+="*";
			}
			return email.substring(0,  1)+asterisk;
		}
	}
	
}

package com.skt.api.common.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;

import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.resource.CodeConstants.AUTH_CODE;
import com.skt.api.common.resource.CodeConstants.RESULT_CODE;

public class CommonController {
	protected static Logger log = Logger.getLogger(CommonController.class.getName()); 
	
	public void setErrorHeader( String key, String value, HttpServletResponse response )
	{
    	response.setHeader(key, value);
	}
	

	@SuppressWarnings("unchecked")
	public Object writeGrantError( AUTH_CODE code, HttpStatus status, HttpServletResponse response )
	{
		/**
		if(code==Constants.AUTH_CODE.INVAID_BASIC)
	    	response.setHeader("WWW-Authenticate", 
	        	"Bearer realm=\""+Constants.REALM+"/client\"");
		else if(code==Constants.AUTH_CODE.INVAID_REQUEST || code==Constants.AUTH_CODE.INVAID_GRANT || code==Constants.AUTH_CODE.SERVER_ERROR)
			;
		else
		**/
    	response.setHeader("WWW-Authenticate", 
    		"Bearer realm=\""+CodeConstants.RESOURCE+"\", "+
    		"error=\""+code.getValue()[0]+"\", "+
    		"error_description=\""+code.getValue()[1]+"\"");

		JSONObject resp = new JSONObject();

		resp.put("error", code.getValue()[0] );
		resp.put("error_description", code.getValue()[1] );

		log.error("CommonController.writeGrantError >>>>>>>>>>>>"+resp.toString()+"\n");

		byte[] b = null;
		try {
			b = resp.toString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.fatal("CommonController.writeGrantError >>>>>>>>>>>>"+resp.toString()+"\n");
		};

		response.setStatus(status.value());
	    writeJSONToRes(response, b);
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public Object writeJSONToRes( RESULT_CODE code, JSONObject res_data, HttpServletResponse response )
	{
		JSONObject resp = new JSONObject();

		JSONObject common = new JSONObject();
		common.put("resultCode", code.name() );
		common.put("resultMsg", code.getValue() );
		common.put("trxId", "" );
		resp.put("common", common );
		
		resp.put("data", res_data );
		if(CodeConstants.RESULT_CODE.API2000 != code)
			log.error("CommonController.writeJSONToRes >>>>>>>>>>>>"+resp.toString()+"\n");
		else
			log.info("CommonController.writeJSONToRes >>>>>>>>>>>>"+resp.toString()+"\n");
		
		byte[] b = null;
		try {
			b = resp.toString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			common.put("resultCode", CodeConstants.RESULT_CODE.API4999.name() );
			common.put("resultMsg", CodeConstants.RESULT_CODE.API4999.getValue() );
			log.fatal("CommonController.writeJSONToRes >>>>>>>>>>>>"+resp.toString()+"\n");
		};

		writeJSONToRes(response, b);
		
		return null;
	}
	
	public boolean writeJSONToRes(HttpServletResponse response, byte[] b) {
		response.setContentType("application/json; charset=UTF-8");
		OutputStream sos;
		try {
			sos = response.getOutputStream();
	    	sos.write(b,0,b.length); 
	    	sos.flush();
		} catch (IOException e) {
			log.fatal("CommonController.writeJSONToRes >>>>>>>>>>>>"+e.getMessage()+"\n");
			return false;
		}
		
		return true;
	}

}

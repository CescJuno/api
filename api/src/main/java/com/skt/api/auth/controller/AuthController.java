package com.skt.api.auth.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skt.api.common.controller.CommonController;
import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.util.PropertyResources;
import com.skt.api.common.util.SHA512Crypt;
import com.skt.api.common.util.SessionFactory;
import com.skt.api.user.vo.User;

@RestController
public class AuthController extends CommonController {
	protected static Logger log = Logger.getLogger(AuthController.class.getName()); 
	
	@Resource(name = "sessionFactory")
	private SessionFactory session;
	
    @Autowired
    private SqlSessionTemplate sqlSession;

	@SuppressWarnings("unchecked")
	@RequestMapping(value="/oauth/token", produces="application/json; charset=utf-8")
	public void getOAuthTokeln(HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.debug("getOAuthToken");
		
        String accept= request.getHeader("Accept");
        log.debug("accept="+accept);
        
        String authorization= request.getHeader("Authorization");
        log.debug("authorization="+authorization);
        
        if(accept.equals("*/*") || authorization == null) {
        	writeGrantError(CodeConstants.AUTH_CODE.INVAID_BASIC, HttpStatus.UNAUTHORIZED, response);
        	return;
        }

        String[] sa = authorization.split("\\s", 2);
        if(!sa[0].trim().equals("Basic") || sa.length < 2) {
        	writeGrantError(CodeConstants.AUTH_CODE.INVAID_BASIC, HttpStatus.UNAUTHORIZED, response);
        	return;
        }
        
        
    	String code= new String(Base64.getDecoder().decode(sa[1].trim()));
    	String[] codes = code.split(":");
    	if(!codes[0].equals(CodeConstants.CLIENT)) {
        	writeGrantError(CodeConstants.AUTH_CODE.INVAID_BASIC, HttpStatus.UNAUTHORIZED, response);
        	return;
       }
    	
    	Map<String,Object> paramMap = (Map<String,Object>) request.getParameterMap();
    	for (Map.Entry<String, Object> entry: paramMap.entrySet())
    		log.debug("key : " + entry.getKey() + " / value : " + ((String[])entry.getValue())[0]);
    	
    	Object grantType = paramMap.get("grant_type");
    	if(grantType != null && grantType instanceof String[])
    		grantType = ((String[])grantType)[0];
    	
		if(grantType == null) {
        	writeGrantError(CodeConstants.AUTH_CODE.INVAID_REQUEST, HttpStatus.BAD_REQUEST, response);
        	return;
        }
    	
		JSONObject token = null;
    	if(grantType.toString().equals(CodeConstants.GRANT_TYPE.PASSWORD.getValue())) {
    		String username= ((String[]) paramMap.get("username"))[0];
    		String password= ((String[]) paramMap.get("password"))[0];
        	if(username == null || password == null) {
            	writeGrantError(CodeConstants.AUTH_CODE.INVAID_GRANT, HttpStatus.BAD_REQUEST, response);
            	return;
            }


    		Map<String, Object> paramMap1 = new HashMap<String, Object>();
    		paramMap1.put("userId", username);
    		paramMap1.put("userPwd", SHA512Crypt.getEncrypt(password));
    		
    		User userInfo = sqlSession.selectOne("user.selectUserByEmailPwd", paramMap1);
    		if(userInfo == null) {
    			//writeGrantError(CodeConstants.AUTH_CODE.INVAID_REQUEST, HttpStatus.BAD_REQUEST, response);
    			writeGrantError(CodeConstants.AUTH_CODE.INVAID_GRANT, HttpStatus.BAD_REQUEST, response);
            	return;
    		}
    		
        	if((token = writeGrantResult(username, null, response))==null)
        		return;
        	

            ///SessionFactory session = SessionFactory.getInstance();
            session.setSession(token.get("access_token").toString(), token, false);
            session.setSession(token.get("refresh_token").toString(), token, true);
    	}
    	else if(grantType.toString().equals(CodeConstants.GRANT_TYPE.REFRESH_TOKEN.getValue())) {
        	if(paramMap.get("refresh_token") == null) {
            	writeGrantError(CodeConstants.AUTH_CODE.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR, response);
            	return;
            }

    		String refreshToken = ((String[]) paramMap.get("refresh_token"))[0];
            ///SessionFactory session = SessionFactory.getInstance();
            token = (JSONObject) session.getSession(refreshToken);
        	
            if(token == null || !refreshToken.equals(token.get("refresh_token").toString())) {
            	writeGrantError(CodeConstants.AUTH_CODE.INVAID_REFRESH, HttpStatus.UNAUTHORIZED, response);
            	return;
            }
            
        	if((token = writeGrantResult(null, token, response))==null)
        		return;
        	
            session.setSession(token.get("access_token").toString(), token, false);
        }
		///response.setStatus(HttpStatus.OK.value());
	}

	@SuppressWarnings("unchecked")
	private JSONObject writeGrantResult( String userId, JSONObject oldToken, HttpServletResponse response )
	{
		int accessExpires = 0;
		int refreshExpires = 0;
		try {
			accessExpires = Integer.parseInt(PropertyResources.getInstance().getProperty("token.access.expires"));
			refreshExpires = Integer.parseInt(PropertyResources.getInstance().getProperty("token.refresh.expires"));
		} catch (Throwable e1) {
			log.fatal("Not exists token.access.expires or token.refresh.expires in properties file!");
			writeGrantError(CodeConstants.AUTH_CODE.INVAID_BASIC, HttpStatus.UNAUTHORIZED, response);
			return null;
		}

		if(oldToken != null) {
	        String stringToConvert = String.valueOf(oldToken.get("refresh_expires_time"));
	        Long refreshExpiresTime = Long.parseLong(stringToConvert);
			Calendar now = Calendar.getInstance();
			if(now.getTimeInMillis() > refreshExpiresTime) {
	        	writeGrantError(CodeConstants.AUTH_CODE.EXPIRED_REFRESH, HttpStatus.UNAUTHORIZED, response);
	        	return null;
	        }
		}
		
		JSONObject token = oldToken != null? oldToken : new JSONObject();
		token.put("access_token", UUID.randomUUID().toString().replaceAll("-", ""));
		
		Calendar now = Calendar.getInstance();
		now.add(Calendar.SECOND, accessExpires);
		token.put("access_expires_time", now.getTimeInMillis());
		
		if(oldToken == null) {	//grant_type=password
			token.put("refresh_token", UUID.randomUUID().toString().replaceAll("-", ""));
			token.put("token_type", "bearer");
			token.put("expires_in", accessExpires);
			token.put("scope", "read write trust");
			
			now = Calendar.getInstance();
			now.add(Calendar.SECOND, refreshExpires);
			token.put("refresh_expires_time", now.getTimeInMillis());
			token.put("userId", userId);
		}
		
		JSONObject resp = new JSONObject();
		resp.put("access_token", token.get("access_token"));
		resp.put("refresh_token", token.get("refresh_token"));
		resp.put("token_type", token.get("token_type"));
		resp.put("expires_in", token.get("expires_in"));
		resp.put("scope", token.get("scope"));

		log.debug("AuthController.writeGrantResult >>>>>>>>>>>>"+resp.toString()+"\n");

		byte[] b = null;
		try {
			b = resp.toString().getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			log.fatal("AuthController.writeGrantResult >>>>>>>>>>>>"+resp.toString()+"\n");
		};

	    if(!writeJSONToRes(response, b))
	    	return null;
		
        return token;
	}

}

package com.skt.api.common.authorization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.skt.api.common.controller.CommonController;
import com.skt.api.common.resource.CodeConstants;
import com.skt.api.common.util.SessionFactory;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    protected Log log = LogFactory.getLog(AuthInterceptor.class);

	@Resource(name = "sessionFactory")
	private SessionFactory session;

    @SuppressWarnings("unchecked")
	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("AuthInterceptor Request URI \t:  " + request.getRequestURI());
        }


		/*
		Map<String, Object> reqMap = setReqToJSON(request, response);
		if(reqMap == null) {
			log.fatal("No request parameter!");
			new CommonController().writeJSONToRes(Constants.RESULT_CODE.OCR3001, null, response);
			return false;
		} 
		
		Map<String, Object> commonMap = (Map<String, Object>)reqMap.get("common");
		Map<String, Object> dataMap = (Map<String, Object>)reqMap.get("data");
		request.getSession().setAttribute("commonMap", commonMap);
		request.getSession().setAttribute("dataMap", dataMap);
        
		*/
		return checkAuthorization(request, response);
        //return true;
    }

	@SuppressWarnings("unused")
	private boolean checkAuthorization(HttpServletRequest request, HttpServletResponse response) {
		//header의 Authorization 값을 가져옴
		String authorization= request.getHeader("Authorization");
		
		//header에 authorization이 없고 파라미터로 넘어온 access_token이 있는 경우
        if(authorization == null && request.getParameter("access_token") != null)
        	authorization = "Bearer "+request.getParameter("access_token");
        
        log.debug("authorization="+authorization);
        
        //header에 authorization도 없고 access_token으로 넘어온 값도 없는 경우
        if(authorization == null) {
        	new CommonController().writeGrantError(CodeConstants.AUTH_CODE.INVAID_GRANT, HttpStatus.BAD_REQUEST, response);
        	return false;
        }
        
        //Bearer access_token정보를 split으로 배열에 저장함
        String[] sa = authorization.split("\\s", 2);
        
        //Bearer access_token 정보가 아닌 값이 들어올 경우
        if(!sa[0].trim().equals("Bearer") || sa.length < 2) {
        	new CommonController().writeGrantError(CodeConstants.AUTH_CODE.INVAID_GRANT, HttpStatus.BAD_REQUEST, response);
        	return false;
        }
        
        //access_token 정보 추출
        String accessToken = sa[1].trim();
        
        ///SessionFactory session = SessionFactory.getInstance();

        
        JSONObject token = (JSONObject) session.getSession(accessToken);
        //Redis에 해당 access_token으로 기 저장된 토큰값이 있는지 화인
        if(token == null || !accessToken.equals(token.get("access_token").toString())) {
        	new CommonController().writeGrantError(CodeConstants.AUTH_CODE.INVAID_ACCESS, HttpStatus.UNAUTHORIZED, response);
        	return false;
        }
        
        //Redis에 해당 access_token으로 기 저장된 만료일을 가져옴
        String stringToConvert = String.valueOf(token.get("access_expires_time"));
        Long accessExpiresTime = Long.parseLong(stringToConvert);
		Calendar now = Calendar.getInstance();
		
		//Redis에 해당 access_token으로 기 저장된 만료일이 현재날짜보다 작을경우 예외처리
		if(now.getTimeInMillis() > accessExpiresTime) {
        	new CommonController().writeGrantError(CodeConstants.AUTH_CODE.EXPIRED_ACCESS, HttpStatus.UNAUTHORIZED, response);
        	return false;
        }
		
		//Redis에 해당 access_token으로 기 저장된 사용자 ID 정보를 가져와서 보여줌
		//dataMap.put("userId", token.get("userId"));
		//request.getSession().setAttribute("token", token);
		return true;
	}
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

}

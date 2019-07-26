package com.skt.api.common.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*import org.apache.http.client.entity.UrlEncodedFormEntity;*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.skt.api.common.util.StringUtil;

@Controller
@RequestMapping(value = "/external", method = {RequestMethod.GET, RequestMethod.POST})
public class ProxyController
{
	private final Integer API_TIMEOUT = 20000;

	private static ProxyController instance;
	private static final Logger logger = LoggerFactory.getLogger(ProxyController.class);

	public static ProxyController getInstance() {

		if (instance == null)
			instance = new ProxyController();

		return instance;
	}
	
	@RequestMapping(value = {"/log/","log"})
	public void log(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		ByteArrayOutputStream bos= new ByteArrayOutputStream();
		int numRead; 
	    byte b[] = new byte[1024];
		ServletInputStream sis = request.getInputStream();
		while((numRead = sis.read(b,0,1024)) != -1){ 
	    	bos.write(b,0,numRead); 
	    }
		String bstr = bos.toString();
		bos.close();
		logger.debug("ProxyController.log:"+bstr);
	}
	
	@RequestMapping(value = {"/ajax/","ajax"})
	public void ajax(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		String context = request.getParameter("_context");
		logger.info("ProxyController.ajax("+context+") >>>>>>>>>>>>");
		Map<String, Object> param = StringUtil.parse(context);
		
	    call(request, response, param);
	}

	@RequestMapping(value = {"/submit/","submit"})
	public void submit(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		ByteArrayOutputStream bos= new ByteArrayOutputStream();
		int numRead; 
	    byte b[] = new byte[1024];
		ServletInputStream sis = request.getInputStream();
		while((numRead = sis.read(b,0,1024)) != -1){ 
	    	bos.write(b,0,numRead); 
	    }
		String bstr = bos.toString();
		bos.close();
		
		logger.info("ProxyController.submit("+bstr+") >>>>>>>>>>>>");
		Map<String, Object> param = StringUtil.parse(bstr);

	    call(request, response, param);
	}

	synchronized public String exec(Map<String, Object> param) {
		String method = param.get("method")==null||param.get("method").toString().equals("")? "POST":param.get("method").toString();
		String encoding = param.get("encoding")==null||param.get("encoding").equals("")? "UTF-8":param.get("encoding").toString();
		String contentType = param.get("contentType")==null||param.get("contentType").equals("")? "application/json;charset=UTF-8":param.get("contentType").toString();
		boolean hasInput = (boolean)param.get("hasInput")? true:false;
		Map<String, Object> header = null;
		try {
			header = param.get("header")==null? null: StringUtil.parse(param.get("header").toString());
		} catch (Exception e1) {}
		String inputText = param.get("inputText")==null||param.get("inputText").equals("")? "": param.get("inputText").toString();
		boolean hasOutput = (boolean)param.get("hasOutput")? true:false;
		boolean debug = (boolean)param.get("debug")? true:false;
		String strUrl = "";
		String url = (String)param.get("url");
		if(url != null && !url.equals(""))
			strUrl += url;
		
	    byte b[] = new byte[1024];
		InputStream sis = new ByteArrayInputStream(b);
		OutputStream sos = new ByteArrayOutputStream();
		
		try {
			view(method, encoding, contentType, hasInput, hasOutput, debug, strUrl, header, inputText, sis, sos);
			String bstr = new String(((ByteArrayOutputStream) sos).toByteArray(), encoding);
			bstr = sos.toString();
			sos.close();
			return bstr;
		} catch (IOException e) {
			return null;
		}
	}

	private void call(HttpServletRequest request, HttpServletResponse response, Map<String, Object> param)
			throws IOException {
		String method = param.get("method")==null||param.get("method").toString().equals("")? "POST":param.get("method").toString();
		String encoding = param.get("encoding")==null||param.get("encoding").equals("")? "UTF-8":param.get("encoding").toString();
		String contentType = param.get("contentType")==null||param.get("contentType").equals("")? "application/json;charset=UTF-8":param.get("contentType").toString();
		boolean hasInput = param.get("hasInput").equals("false")? false:true;
		Map<String, Object> header = param.get("header")==null? null: StringUtil.parse(param.get("header").toString());
		String inputText = param.get("inputText")==null||param.get("inputText").equals("")? "": (String) param.get("inputText");
		boolean hasOutput = param.get("hasOutput").equals("false")? false:true;
		boolean debug = param.get("debug").equals("false")? false:true;
		String strUrl = "";
		String url = (String)param.get("url");
		if(url != null && !url.equals(""))
			strUrl += url;
		
		InputStream sis = request.getInputStream();
		OutputStream sos = response.getOutputStream();
		response.setContentType("text/html;charset="+encoding);
		
		view(method, encoding, contentType, hasInput, hasOutput, debug, strUrl, header, inputText, sis, sos);
	}

	private void view(String method, String encoding, String contentType, boolean hasInput, boolean hasOutput, boolean debug, String strUrl, Map<String, Object> header, String inputText, InputStream sis, OutputStream sos)
			throws IOException {
		
		HttpURLConnection httpConnect = null;
		//UrlEncodedFormEntity p_entity;

		try {
			//p_entity = new UrlEncodedFormEntity(pairs, "utf-8");
			URL url = new URL(strUrl);

			//if (mProxy != null) {
			//	httpConnect = (HttpURLConnection) url.openConnection(mProxy);
			//} else {
			//	httpConnect = (HttpURLConnection) url.openConnection();
			//}
			httpConnect = (HttpURLConnection) url.openConnection();
			logger.info("view : open connection to http!");
			
			httpConnect.setDoInput(true);
			httpConnect.setDoOutput(true);
			httpConnect.setUseCaches(false);
			httpConnect.setAllowUserInteraction(false);
			httpConnect.setRequestMethod( method );
			httpConnect.setConnectTimeout( API_TIMEOUT );
			httpConnect.setReadTimeout( API_TIMEOUT );
			httpConnect.setRequestProperty("Accept", contentType);
			httpConnect.setRequestProperty("Content-Type", contentType);
			if(header != null) {
				for (Object skey : (Object[]) header.keySet().toArray()) {
					String value = (String) header.get((String)skey);
					httpConnect.setRequestProperty((String)skey, value);
					logger.debug("header : "+(String)skey +":"+ value);
				}
			}
			
			//httpConnect.addRequestProperty("Content-type", "text/html;charset=utf-8");
			//		"application/x-www-form-urlencoded;charset=utf-8");

			httpConnect.connect();
			logger.info("view : connected to http!");

			String bstr = null;
			ByteArrayOutputStream bos=null;
			if(debug)
				bos=new ByteArrayOutputStream();
			//p_entity.writeTo(os);
			int numRead; 
		    byte b[] = new byte[1024];
		    
		    if (hasInput) {
				OutputStream hos = httpConnect.getOutputStream();
				logger.debug("view : get http output stream!");

				if(!inputText.equals("")) {
					b = inputText.getBytes(encoding);
					numRead = b.length;
			    	hos.write(b,0,numRead); 
					if(debug)
						bos.write(b,0,numRead); 
				} else {
					logger.debug("view : get servlet input stream!");

					while((numRead = sis.read(b,0,1024)) != -1){ 
				    	hos.write(b,0,numRead); 
						if(debug)
							bos.write(b,0,numRead); 
				    }
				}
				hos.flush();
				if(debug) {
					bstr = bos.toString();
					bos.close();
				}
				logger.debug("view : send "+strUrl);
				if(debug)
					logger.debug("view : data to http!-->\n"+bstr);
		    }

		    if (hasOutput) {
				InputStream his = httpConnect.getInputStream();
				logger.debug("view : get http input stream!");
				
				logger.debug("view : get servlet output stream!");
				
				//strResponse = BaseHelper.convertStreamToString(content);
				//BaseHelper.log(TAG, "response " + strResponse);
				
				if(debug)
					bos=new ByteArrayOutputStream();
			    while((numRead = his.read(b,0,1024)) != -1){ 
			    	sos.write(b,0,numRead); 
					if(debug)
						bos.write(b,0,numRead); 
			    }
			    sos.flush();
				if(debug) {
					bstr = new String(bos.toByteArray(), encoding);
					//bstr = bos.toString();
					bos.close();
				}
				logger.info("view : read from "+strUrl);
				if(debug)
					logger.debug("view : data from http!-->\n"+bstr);
		    }
		} catch (IOException e) {
			logger.error("view("+strUrl+") Error:"+e.getMessage());
			
			String err = "{\"common\":{\"resultCode\":\"Proxy Exception\", \"resultMsg\":\""+e.getMessage()+"\"},\"data\":{}}";
			sos.write(err.getBytes());
			sos.flush();
			//return null;
			return;
		} finally {
			httpConnect.disconnect();
		}
	}
	
}

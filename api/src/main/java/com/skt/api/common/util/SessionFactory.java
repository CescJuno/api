package com.skt.api.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import com.skt.api.common.redis.RedisInfo;
import com.skt.api.common.util.PropertyResources;

///@Component("sessionFactory")
public class SessionFactory /*implements FactoryBean<Object> */{
	///private static SessionFactory instance;

	private static Logger log = Logger.getLogger(SessionFactory.class.getName()); 
	private static JSONObject session = null;
	private static long accessTimeout = 0L;
	private static long refreshTimeout = 0L;
	private static String storage = null;

	private RedisInfo redisInfo;

	//final long LIMIT = 86400000L;	//24hour in milli-seconds
	final long LIMIT = 10000L;	//24hour in milli-seconds
	long lastCleanTime = 0L;
	
	/**
	public static SessionFactory getInstance() {
		if (instance == null) {
			instance = new SessionFactory();
			session = new JSONObject();
			
			try {
				PropertyResources pr = PropertyResources.getInstance();
				accessTimeout = Long.parseLong(pr.getProperty("token.access.timeout"));
				refreshTimeout = Long.parseLong(pr.getProperty("token.refresh.timeout"));
				storage = pr.getProperty("session.storage");
			} catch (Throwable e1) {
				log.fatal("Not exists token.access.timeout or token.refresh.timeout or session.storage in properties file!");
				return null;
			}
		}

		return instance;
	}**/
	
	public SessionFactory() throws Exception {
		log.debug("SessionFactory constructor");
		session = new JSONObject();
		try {
			PropertyResources pr = PropertyResources.getInstance();
			accessTimeout = Long.parseLong(pr.getProperty("token.access.timeout"));
			refreshTimeout = Long.parseLong(pr.getProperty("token.refresh.timeout"));
			storage = pr.getProperty("session.storage");
		} catch (Throwable e1) {
			throw new Exception("Not exists token.access.timeout or token.refresh.timeout or session.storage in properties file!");
		}
	}
	
	public Object getSession(String skey) {
		if(storage.equals("redis")) {
			try {
				return redisInfo.getRedis(skey);
			} catch (NullPointerException e) {
				return null;
			}
		}
		
		if(session.get(skey)==null)
			return null;
		return session.get(skey);
	}
	
	@SuppressWarnings("unchecked")
	public void setSession(String skey, Object val, boolean isRefresh) {
		if(storage.equals("redis")) {
			redisInfo.setRedis(skey, (JSONObject) val, isRefresh? refreshTimeout : accessTimeout);
			return;
		}
		
		session.put(skey, val);
		
		///clearSession();
	}

	private void clearSession() {
		Calendar now = Calendar.getInstance();
		long currentTime = now.getTimeInMillis();
		if(currentTime - lastCleanTime < LIMIT)
			return;
		
		lastCleanTime= currentTime;
		long refreshExpiresTime = 0;
		List<String> cleans = new ArrayList<String>();
		try {
			for (String skey : (String[]) session.keySet().toArray()) {
				@SuppressWarnings("unchecked")
				Map<String,Object> token = (Map<String,Object>) session.get(skey);
				//refreshExpiresTime = (long)token.get("refresh_expires_time");
				if(currentTime - refreshExpiresTime > LIMIT)
					cleans.add(skey);
			}
			for (String key : cleans) {
				session.remove(key);
			}
		} catch (Throwable e1) {
			log.fatal("clear properties error!");
			return;
		}
	}

}

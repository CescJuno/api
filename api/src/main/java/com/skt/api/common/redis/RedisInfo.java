package com.skt.api.common.redis;

import org.json.simple.JSONObject;

public interface RedisInfo
{
	public JSONObject getRedis(String skey);
	public void setRedis(String skey, JSONObject val, long timeout);
	public void clearRedis(String skey);
}

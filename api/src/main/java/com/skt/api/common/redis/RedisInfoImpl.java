package com.skt.api.common.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

@Component("redisInfo")
public class RedisInfoImpl implements RedisInfo
{
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(RedisInfoImpl.class);

	@Resource(name = "redisTemplate0")
	private ValueOperations<String, String> valuesOps;

	@Override
	public JSONObject getRedis(String skey)
	{
		logger.debug("\n###########################################\ngetRedisSessionInfo start:"+skey);

		String str_from_redis = valuesOps.get(skey).trim();

		logger.debug("getRedisSessionInfo JSON_FROM_REDIS = " + str_from_redis );

		if(str_from_redis == null || str_from_redis.isEmpty() || str_from_redis.equals("{}") )
			return null;

		JSONObject json;
		try {
			json = (JSONObject)new JSONParser().parse(str_from_redis);
		} catch (ParseException e) {
			logger.error("getRedisSessionInfo json parse error !");
			return null;
		}

		logger.debug("getRedisSessionInfo END\n###########################################");

		return json;
	}
	
	@Override
	public void setRedis(String skey, JSONObject val, long timeout)
	{
		logger.debug("\n###########################################\nsetRedisSessionInfo start:"+skey);
		logger.debug("setRedisSessionInfo session = " + val.toString());

		valuesOps.set(skey, val.toString());
		valuesOps.getOperations().expire(skey, timeout, TimeUnit.SECONDS);

		logger.debug("setRedisSessionInfo end\n###########################################");
	}
	
	@Override
	public void clearRedis(String skey)
	{
		logger.debug("\n###########################################\nclearRedisSessionInfo start:"+skey);
		valuesOps.set(skey, "{}");
		logger.debug("clearRedisSessionInfo end\n###########################################");
	}
}
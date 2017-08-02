package com.zhp.bos.redis;

import java.util.Set;

import com.alibaba.fastjson.serializer.SerializeFilter;

public interface RedisCRUD {
	/**
	 * 基于fastjson完成json字符串存储到redis数据库上
	 * 
	 * @param key
	 * @param jsonString
	 */
	/**
	 * 
	 * @param key
	 * @param jsonString
	 *            json字符串
	 */
	public void writeJSONStringToRedis(String key, String jsonString);

	/**
	 * 
	 * @param key
	 * @param obj普通数据
	 */
	public void writeObjectToRedisByFastJSON(String key, Object obj);

	/**
	 * 序列化(过滤器)+数据保存到redis
	 * 
	 * @param key
	 * @param obj普通数据
	 * @param filter普通数据序列化的过滤器（那些数据需要过滤,那些数据不需要过滤都保存再filter中）
	 */
	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, SerializeFilter filter);

	/**
	 * 序列化(需要过滤的属性)+数据保存到redis
	 * 
	 * @param key
	 * @param obj
	 * @param inproperties
	 *            需要序列化的数据
	 */
	public void writeObjectToRedisByFastJSONIncludes(String key, Object obj, String... inproperties);

	/**
	 * 从reids中获取数据
	 * 
	 * @param key
	 * @return
	 */
	public String GetJSONStringFromRedis(String key);

	/**
	 * 删除redis中指定键的数据
	 * 
	 * @param key
	 */
	public void deleteJSONStringFromRedisByKey(String key);

	/**
	 * 获取所有的key
	 * 
	 * @param key
	 */
	public Set<String> getAllKey(String pattern);

}

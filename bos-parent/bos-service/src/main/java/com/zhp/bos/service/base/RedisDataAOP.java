package com.zhp.bos.service.base;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.zhp.bos.entity.bc.Region;
import com.zhp.bos.redis.RedisCRUD;

@Component
@Aspect
public class RedisDataAOP {
	@Autowired
	private RedisCRUD redisCRUD;

	@Around("bean(*ServiceImpl)")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		String name = proceedingJoinPoint.getSignature().getName();
		String target = proceedingJoinPoint.getTarget().getClass().getName();
		// NoPredicaTePageQuery --无条件查询方法名
		if ("NoPredicaTePageQuery".equalsIgnoreCase(name)) {
			PageRequest pageRequest = (PageRequest) proceedingJoinPoint.getArgs()[0];
			int page = pageRequest.getPageNumber();
			int rows = pageRequest.getPageSize();
			String key = target + "_" + page + "_" + rows;// 保证key不重复
			String jsonString = redisCRUD.GetJSONStringFromRedis(key);
			if (StringUtils.isNotBlank(jsonString)) {
				// redis内存在对应数据
				return jsonString;
			} else {
				//// redis内不存在对应数据，查询数据库，并使用fastjson序列化数据并放入redis中
				// 条件查询的返回值类型必须是object，因为查询结果为page类型，而目标方法返回值为json格式字符串
				Page<Region> pages = (Page<Region>) proceedingJoinPoint.proceed();
				Map<String, Object> map = new HashMap<>();
				map.put("total", pages.getTotalElements());
				map.put("rows", pages.getContent());
				String jsonString2 = JSON.toJSONString(map);
				redisCRUD.writeJSONStringToRedis(key, jsonString2);
				return jsonString2;
			}
		} else if (name.startsWith("save") || name.startsWith("delete") || name.startsWith("update")) {
			// 增删改必须以上面字符开头
			Object proceed = proceedingJoinPoint.proceed();
			Set<String> allKey = redisCRUD.getAllKey(target + "*");
			if (allKey != null && allKey.size() != 0) {
				for (String key : allKey) {
					redisCRUD.deleteJSONStringFromRedisByKey(key);
				}
			}
			return proceed;
		} else {
			return proceedingJoinPoint.proceed();
		}

	}

}

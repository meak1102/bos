package com.zhp.bos.service.impl.city;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhp.bos.dao.city.LoadCityDao;
import com.zhp.bos.entity.city.City;
import com.zhp.bos.redis.RedisCRUD;
import com.zhp.bos.service.intf.city.ILoadCityService;

@Service
@Transactional
public class LoadCityServiceImpl implements ILoadCityService {
	@Autowired
	private LoadCityDao loadCityDao;
	@Autowired
	private RedisCRUD redisCRUD;

	@Override
	public String load(City model) {
		String jsonString = redisCRUD.GetJSONStringFromRedis("city_" + model.getPid());
		if (jsonString == null) {
			List<City> cities = loadCityDao.findByPid(model.getPid());
			String jsonString2 = JSON.toJSONString(cities);
			redisCRUD.writeJSONStringToRedis("city_" + model.getPid(), jsonString2);
			return jsonString2;
		} else
			return jsonString;
	}

}

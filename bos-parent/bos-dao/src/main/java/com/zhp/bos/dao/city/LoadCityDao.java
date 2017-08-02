package com.zhp.bos.dao.city;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.city.City;

public interface LoadCityDao extends JpaRepository<City, Integer> {
	@Query("from City where pid=?1")
	List<City> findByPid(Integer pid);

}

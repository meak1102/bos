package com.zhp.bos.dao.bc;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.bc.Region;

public interface RegionDao extends JpaRepository<Region, String>, JpaSpecificationExecutor<Region> {
	@Query("from Region where province like %?1% or city like %?1% or district like %?1%")
	List<Region> regionList(String q);

	@Query("from Region where province=?1 and city =?2 and district=?3")
	Region findByProvinceAndCityAndDistrict(String province, String city, String district);

}

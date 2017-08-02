package com.zhp.bos.service.intf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Region;

public interface IRegionService {

	void oneCilckUpload(List<List<Region>> list);

	Page<Region> pageQuery(Specification<Region> spec, PageRequest pageRequest);

	void save(Region model);

	void delete(String[] idsarr);

	List<Region> regionList(String q);

}

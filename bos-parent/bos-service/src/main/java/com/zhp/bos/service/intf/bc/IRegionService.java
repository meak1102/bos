package com.zhp.bos.service.intf.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Region;

public interface IRegionService {
	/**
	 * 一键上传
	 * 
	 * @param list
	 */

	void oneCilckUpload(List<List<Region>> list);

	/**
	 * 条件分页查询
	 * 
	 * @param spec
	 * @param pageRequest
	 * @return
	 */
	Page<Region> pageQuery(Specification<Region> spec, PageRequest pageRequest);

	/**
	 * 无条件分页查询
	 * 
	 * @param pageRequest
	 * @return
	 */
	Object NoPredicaTePageQuery(PageRequest pageRequest);

	/**
	 * 区域保存
	 * 
	 * @param model
	 */
	void save(Region model);

	/**
	 * 区域删除
	 * 
	 * @param idsarr
	 */
	void delete(String[] idsarr);

	/**
	 * 区域查询
	 * 
	 * @param q
	 * @return
	 */

	List<Region> regionList(String q);

	List<Region> findAll();

}

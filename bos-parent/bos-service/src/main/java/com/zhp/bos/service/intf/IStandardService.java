package com.zhp.bos.service.intf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.zhp.bos.entity.bc.Standard;

public interface IStandardService {
	/**
	 * 标准添加
	 * 
	 * @param standard
	 */
	public void save(Standard standard);

	/**
	 * 无条件分页查询所有标准
	 * 
	 * @param standard
	 */
	public Page<Standard> pageQuery(Pageable pageable);

	/**
	 * 标准作废
	 * 
	 * @param standard
	 */
	public void deltag(String[] idsarr);

	/**
	 * 标准启用
	 * 
	 * @param standard
	 */
	public void usedtag(String[] idsarr);

	/**
	 * 查找所有已启用的标准
	 * 
	 * @return
	 */
	public List<Standard> findAllInUse();

}

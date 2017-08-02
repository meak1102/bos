package com.zhp.bos.service.intf;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Subarea;

public interface ISubareaService {
	/**
	 * 一键上传
	 * 
	 * @param list
	 */
	void oneCilckUpload(List<List<Subarea>> list);

	/**
	 * 分区保存
	 * 
	 * @param model
	 */
	void save(Subarea model);

	/**
	 * 无条件查询
	 * 
	 * @param spec
	 * 
	 * @param pageRequest
	 * @return
	 */
	Page<Subarea> pageQuery(Specification<Subarea> spec, PageRequest pageRequest);

	/**
	 * 条件删除
	 * 
	 * @param idsarr
	 */
	void delete(String[] idsarr);

	/**
	 * 数据导出
	 * 
	 * @param specification
	 * @return
	 */
	List<Subarea> importDat(Specification<Subarea> specification);

	/**
	 * 查询所有未分定区的分区以及指定定区id所关联的分区数据
	 * 
	 * @param hiddenId
	 * 
	 * @return
	 */
	List<Subarea> subareaListInAssociation(String hiddenId);

	/**
	 * 定区修改回显分区数据时根据定区id查询其对应的分区数据
	 * 
	 * @param model
	 * @return
	 */
	List<Subarea> relationalSubarea(Subarea model);

}

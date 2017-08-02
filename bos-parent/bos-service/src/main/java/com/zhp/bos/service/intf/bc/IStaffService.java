package com.zhp.bos.service.intf.bc;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Staff;

public interface IStaffService {
	/**
	 * 添加取派员
	 * 
	 * @param model
	 */
	void save(Staff model);

	/**
	 * 分页查询所有取派员
	 * 
	 * @param spec
	 * 
	 * @param pageRequest
	 * @return
	 */
	Page<Staff> pageQuery(Specification<Staff> spec, PageRequest pageRequest);

	/**
	 * 取派员变为离职
	 * 
	 * @param idsarr
	 */
	void deltag(String[] idsarr);

	/**
	 * 取派员变为在职
	 * 
	 * @param idsarr
	 */
	void usedtag(String[] idsarr);

	/**
	 * 电话号码验证
	 * 
	 * @param telephone
	 * @return
	 */
	Staff checkTelephone(String telephone);

	List<Staff> nameListInUse();

}

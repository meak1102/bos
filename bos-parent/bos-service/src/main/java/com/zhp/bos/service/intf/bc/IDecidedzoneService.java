package com.zhp.bos.service.intf.bc;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.customer.Customers;

public interface IDecidedzoneService {
	/**
	 * 定区保存
	 * 
	 * @param model
	 * @param sids
	 */
	void save(Decidedzone model, String[] sids);

	/**
	 * 定区编号校验
	 * 
	 * @param model
	 * @return
	 */
	Decidedzone checkdecidedzoneId(Decidedzone model);

	/**
	 * 定区分页条件查询
	 * 
	 * @param specification
	 * @param pageRequest
	 * @return
	 */
	Page<Decidedzone> pageQuery(Specification<Decidedzone> specification, PageRequest pageRequest);

	/**
	 * 定区删除
	 * 
	 * @param idsarr
	 */
	void delete(String[] idsarr);

	/**
	 * 获取定区关联的客户
	 * 
	 * @param id
	 * 
	 * @return
	 */
	Collection<? extends Customers> getAssociationCustomersByDecided(String id);

	/**
	 * 获取未被关联的客户
	 * 
	 * @return
	 */
	Collection<? extends Customers> getNoAssociationCustomers();

	/**
	 * 更改定区绑定的客户
	 * 
	 * @param id
	 * @param cids
	 */
	void assignedCustomerToDecidedZone(String id, String[] cids);
}

package com.zhp.bos.service.intf.qp;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.qp.WorkBill;

public interface IWorkBillService {

	Page<WorkBill> pageQuery(Specification<WorkBill> specification, PageRequest pageRequest);

	void repeat(String id);

	void cancel(String ids);

}

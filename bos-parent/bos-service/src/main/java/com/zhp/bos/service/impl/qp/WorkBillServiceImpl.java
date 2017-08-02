package com.zhp.bos.service.impl.qp;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.qp.WorkBillDao;
import com.zhp.bos.entity.qp.WorkBill;
import com.zhp.bos.service.intf.qp.IWorkBillService;

@Service
@Transactional
public class WorkBillServiceImpl implements IWorkBillService {
	@Autowired
	private WorkBillDao workBillDao;

	@Override
	public Page<WorkBill> pageQuery(Specification<WorkBill> specification, PageRequest pageRequest) {
		Page<WorkBill> pages = workBillDao.findAll(specification, pageRequest);
		List<WorkBill> content = pages.getContent();
		if (content != null && content.size() != 0) {
			for (WorkBill workBill : content) {
				Hibernate.initialize(workBill.getStaff());
				Hibernate.initialize(workBill.getNoticeBill());
			}
		}
		return pages;
	}

	@Override
	public void repeat(String id) {
		WorkBill workBill = workBillDao.findOne(id);
		workBill.setType("追");
		workBill.setAttachbilltimes(workBill.getAttachbilltimes() + 1);
	}

	@Override
	public void cancel(String ids) {
		String[] arr = ids.split(",");
		if (ids != null && arr.length != 0) {
			for (String id : arr) {
				workBillDao.concelById(id);
			}
		}

	}

}

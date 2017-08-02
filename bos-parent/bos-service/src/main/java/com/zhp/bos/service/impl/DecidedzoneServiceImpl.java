package com.zhp.bos.service.impl;

import java.util.List;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.DecidedzoneDao;
import com.zhp.bos.dao.SubareaDao;
import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.service.intf.IDecidedzoneService;

@Service
@Transactional
public class DecidedzoneServiceImpl implements IDecidedzoneService {
	@Autowired
	private DecidedzoneDao decidedzoneDao;
	@Autowired
	private SubareaDao SubareaDao;

	@Override
	public void save(Decidedzone model, String[] sids) {
		SubareaDao.clearSubarea(model);
		decidedzoneDao.save(model);// 必须先保存，外键约束
		if (sids != null && sids.length != 0) {
			for (String id : sids) {
				SubareaDao.updateDecidedzone(id, model);
			}
		}
	}

	@Override
	public Decidedzone checkdecidedzoneId(Decidedzone model) {
		return decidedzoneDao.findOne(model.getId());
	}

	@Override
	public Page<Decidedzone> pageQuery(Specification<Decidedzone> specification, PageRequest pageRequest) {
		Page<Decidedzone> pages = decidedzoneDao.findAll(specification, pageRequest);
		// 解决懒加载
		List<Decidedzone> content = pages.getContent();
		if (content != null && content.size() != 0) {
			for (Decidedzone d : content) {
				Hibernate.initialize(d.getStaff());

			}
		}
		return pages;
	}

	@Override
	public void delete(String[] idsarr) {
		if (idsarr != null && idsarr.length != 0) {
			for (String id : idsarr) {
				Decidedzone decidedzone = new Decidedzone();
				decidedzone.setId(id);
				SubareaDao.clearSubarea(decidedzone);
				decidedzoneDao.delete(id);
			}
		}

	}

}

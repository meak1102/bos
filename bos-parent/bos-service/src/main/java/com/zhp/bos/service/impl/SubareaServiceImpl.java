package com.zhp.bos.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.SubareaDao;
import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.bc.Subarea;
import com.zhp.bos.service.intf.ISubareaService;

@Service
@Transactional
public class SubareaServiceImpl implements ISubareaService {
	@Autowired
	private SubareaDao subareaDao;

	@Override
	public void oneCilckUpload(List<List<Subarea>> list) {
		if (!(list.isEmpty())) {
			for (List<Subarea> subareas : list) {
				subareaDao.save(subareas);

			}
		}
	}

	@Override
	public void save(Subarea model) {
		subareaDao.save(model);
	}

	/*
	 * @Override public Page<Subarea> pageQuery(PageRequest pageRequest) {
	 * Page<Subarea> pages = subareaDao.findAll(pageRequest); List<Subarea>
	 * content = pages.getContent(); if (content != null && content.size() != 0)
	 * { for (Subarea subarea : content) {
	 * Hibernate.initialize(subarea.getRegion()); } } return pages; }
	 */

	@Override
	public void delete(String[] idsarr) {
		if (idsarr != null || idsarr.length != 0) {
			for (String id : idsarr) {
				subareaDao.delete(id);
			}
		}

	}

	@Override
	public Page<Subarea> pageQuery(Specification<Subarea> spec, PageRequest pageRequest) {
		Page<Subarea> pages = subareaDao.findAll(spec, pageRequest);
		List<Subarea> content = pages.getContent();
		if (content != null && content.size() != 0) {
			for (Subarea subarea : content) {
				Hibernate.initialize(subarea.getRegion());
			}
		}
		return pages;
	}

	@Override
	public List<Subarea> importDat(Specification<Subarea> spec) {
		List<Subarea> list = subareaDao.findAll(spec);
		if (list != null && list.size() != 0) {
			for (Subarea subarea : list) {
				Hibernate.initialize(subarea.getRegion());
			}
		}
		return list;
	}

	@Override
	public List<Subarea> relationalSubarea(Subarea model) {
		// TODO Auto-generated method stub
		List<Subarea> list = subareaDao.relationalSubarea(model.getDecidedzone());
		if (list != null && list.size() != 0) {
			for (Subarea subarea : list) {
				Hibernate.initialize(subarea.getRegion());
				// Hibernate.initialize(subarea.getDecidedzone());
			}
		}
		return list;
	}

	@Override
	public List<Subarea> subareaListInAssociation(String hiddenId) {
		List<Subarea> list;
		if (StringUtils.isNotBlank(hiddenId)) {
			Decidedzone decidedzone = new Decidedzone();
			decidedzone.setId(hiddenId);
			list = subareaDao.subareaListInAssociation(decidedzone);
		} else {
			list = subareaDao.subareaListInAssociation();
		}
		for (Subarea subarea : list) {
			Hibernate.initialize(subarea.getDecidedzone());
		}
		return list;
	}

}

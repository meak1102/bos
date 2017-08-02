package com.zhp.bos.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.RegionDao;
import com.zhp.bos.entity.bc.Region;
import com.zhp.bos.service.intf.IRegionService;

@Service
@Transactional
public class RegionServiceImpl implements IRegionService {
	@Autowired
	private RegionDao regionDao;

	@Override
	public void oneCilckUpload(List<List<Region>> list) {
		if (list.size() != 0) {
			for (List<Region> regions : list) {
				regionDao.save(regions);
			}
		}

	}

	@Override
	public Page<Region> pageQuery(Specification<Region> spec, PageRequest pageRequest) {
		return regionDao.findAll(spec, pageRequest);
	}

	@Override
	public void save(Region model) {
		regionDao.save(model);
	}

	@Override
	public void delete(String[] idsarr) {
		if (idsarr.length > 0) {
			for (String id : idsarr) {
				regionDao.delete(id);
			}
		}
	}

	@Override
	public List<Region> regionList(String q) {
		if (q == null || q == "") {
			return regionDao.findAll();
		} else {
			return regionDao.regionList(q);
		}
	}
}

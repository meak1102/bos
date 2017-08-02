package com.zhp.bos.service.impl.bc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.bc.StandardDao;
import com.zhp.bos.entity.bc.Standard;
import com.zhp.bos.service.intf.bc.IStandardService;

@Service
@Transactional
public class StandardServiceImpl implements IStandardService {
	@Autowired
	private StandardDao standardDao;

	@Override
	public void save(Standard standard) {
		standardDao.save(standard);

	}

	@Override
	public Page<Standard> pageQuery(Pageable pageable) {
		return standardDao.findAll(pageable);
	}

	@Override
	public void deltag(String[] idsarr) {
		for (String id : idsarr) {
			standardDao.deltag(Integer.parseInt(id));
		}

	}

	@Override
	public void usedtag(String[] idsarr) {
		for (String id : idsarr) {
			standardDao.usedtag(Integer.parseInt(id));
		}
	}

	@Override
	public List<Standard> findAllInUse() {
		return standardDao.findAllInUse();
	}

}

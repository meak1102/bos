package com.zhp.bos.service.impl.bc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.bc.StaffDao;
import com.zhp.bos.entity.bc.Staff;
import com.zhp.bos.service.intf.bc.IStaffService;

@Service
@Transactional
public class StaffServiceImpl implements IStaffService {
	@Autowired
	private StaffDao staffDao;

	@Override
	public void save(Staff model) {
		staffDao.save(model);
	}

	@Override
	public Page<Staff> pageQuery(Specification<Staff> spec, PageRequest pageRequest) {
		return staffDao.findAll(spec, pageRequest);
	}

	@Override
	public void deltag(String[] idsarr) {
		for (String id : idsarr) {
			staffDao.deltag(id);
		}

	}

	@Override
	public void usedtag(String[] idsarr) {
		for (String id : idsarr) {
			staffDao.usedtag(id);
		}
	}

	@Override
	public Staff checkTelephone(String telephone) {
		return staffDao.checkTelephone(telephone);
	}

	@Override
	public List<Staff> nameListInUse() {
		return staffDao.nameListInUse();
	}
}

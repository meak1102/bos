package com.zhp.bos.service.impl.auth;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.auth.FunctionDao;
import com.zhp.bos.entity.auth.Function;
import com.zhp.bos.service.intf.auth.IFunctionService;

@Service
@Transactional
public class FunctionServiceImpl implements IFunctionService {
	@Autowired
	private FunctionDao functionDao;

	@Override
	public void save(Function model) {
		functionDao.save(model);
	}

	@Override
	public Page<Function> pageQuery(PageRequest pageRequest) {
		return functionDao.findAll(pageRequest);
	}

	@Override
	public List<Function> ajaxList() {
		return functionDao.findAll();
	}

	@Override
	public void delete(String ids) {
		String[] functionIds = ids.split(",");
		if (functionIds != null && functionIds.length != 0) {
			for (String functionId : functionIds) {
				Function f = functionDao.findOne(functionId);
				f.setRoles(null);
				functionDao.delete(f);
			}
		}
	}

	@Override
	public List<Function> findFunctionByRoleId(String roleId) {
		// TODO Auto-generated method stub
		return functionDao.findFunctionByRoleId(roleId);
	}

	@Override
	public List<Function> findAll() {
		// TODO Auto-generated method stub
		return functionDao.findAll();
	}
}

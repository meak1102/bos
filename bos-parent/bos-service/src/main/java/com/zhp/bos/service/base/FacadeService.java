package com.zhp.bos.service.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhp.bos.service.intf.auth.IFunctionService;
import com.zhp.bos.service.intf.auth.IMenuService;
import com.zhp.bos.service.intf.auth.IRoleService;
import com.zhp.bos.service.intf.bc.IDecidedzoneService;
import com.zhp.bos.service.intf.bc.IRegionService;
import com.zhp.bos.service.intf.bc.IStaffService;
import com.zhp.bos.service.intf.bc.IStandardService;
import com.zhp.bos.service.intf.bc.ISubareaService;
import com.zhp.bos.service.intf.city.ILoadCityService;
import com.zhp.bos.service.intf.qp.INoticeBillService;
import com.zhp.bos.service.intf.qp.IWorkBillService;
import com.zhp.bos.service.intf.user.IUserService;

@Service
public class FacadeService {
	@Autowired
	private IUserService userService;
	@Autowired
	private IStandardService standardService;
	@Autowired
	private IStaffService staffService;

	@Autowired
	private IRegionService regionService;

	@Autowired
	private ISubareaService subareaService;
	@Autowired
	private IDecidedzoneService decidedzoneService;

	@Autowired
	private ILoadCityService loadCityService;

	@Autowired
	private INoticeBillService noticeBillService;

	@Autowired
	private IWorkBillService workBillService;

	@Autowired
	private IFunctionService functionService;

	@Autowired
	private IMenuService menuService;
	@Autowired
	private IRoleService roleService;

	public IUserService getUserService() {
		return userService;
	}

	public IStandardService getStandardService() {
		return standardService;
	}

	public IStaffService getStaffService() {

		return staffService;
	}

	public IRegionService getRegionService() {
		return regionService;
	}

	public ISubareaService getSubareaService() {
		// TODO Auto-generated method stub
		return subareaService;
	}

	public IDecidedzoneService getDecidedzoneService() {
		return decidedzoneService;
	}

	public ILoadCityService getLoadCityService() {
		return loadCityService;
	}

	public INoticeBillService getNoticeBillService() {
		return noticeBillService;
	}

	public IWorkBillService getWorkBillService() {
		// TODO Auto-generated method stub
		return workBillService;
	}

	public IFunctionService getFunctionService() {
		// TODO Auto-generated method stub
		return functionService;
	}

	public IMenuService getMenuService() {
		// TODO Auto-generated method stub
		return menuService;
	}

	public IRoleService getRoleService() {
		// TODO Auto-generated method stub
		return roleService;
	}
}

package com.zhp.bos.service.facade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zhp.bos.service.intf.IDecidedzoneService;
import com.zhp.bos.service.intf.IRegionService;
import com.zhp.bos.service.intf.IStaffService;
import com.zhp.bos.service.intf.IStandardService;
import com.zhp.bos.service.intf.ISubareaService;
import com.zhp.bos.service.intf.IUserService;

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
	private IDecidedzoneService decidedzone;

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

	public IDecidedzoneService getDecidedzone() {
		return decidedzone;
	}
}

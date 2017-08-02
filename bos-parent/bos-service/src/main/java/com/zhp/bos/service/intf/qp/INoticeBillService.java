package com.zhp.bos.service.intf.qp;

import com.zhp.bos.entity.customer.Customers;
import com.zhp.bos.entity.qp.NoticeBill;

public interface INoticeBillService {

	Customers findCustomerByTelephone(NoticeBill model);

	void save(NoticeBill model, String province, String city, String district);

}

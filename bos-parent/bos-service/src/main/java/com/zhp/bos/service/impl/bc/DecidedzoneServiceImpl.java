package com.zhp.bos.service.impl.bc;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.bc.DecidedzoneDao;
import com.zhp.bos.dao.bc.SubareaDao;
import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.customer.Customers;
import com.zhp.bos.service.base.BaseService;
import com.zhp.bos.service.intf.bc.IDecidedzoneService;

@Service
@Transactional
public class DecidedzoneServiceImpl implements IDecidedzoneService {
	@Autowired
	private DecidedzoneDao decidedzoneDao;
	@Autowired
	private SubareaDao SubareaDao;

	@Override
	public void save(Decidedzone model, String[] sids) {
		SubareaDao.clearSubarea(model);// 修改时使用
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
				SubareaDao.clearSubarea(decidedzone);// oid相同默认对象相同
				decidedzoneDao.delete(id);
			}
		}

	}

	@Override
	public Collection<? extends Customers> getAssociationCustomersByDecided(String id) {
		String url = BaseService.CRM_BASIC_URL;
		Collection<? extends Customers> collection = WebClient.create(url + "getAssociationCustomersByDecided/" + id)
				.accept(MediaType.APPLICATION_JSON).getCollection(Customers.class);
		return collection;
	}

	@Override
	public Collection<? extends Customers> getNoAssociationCustomers() {
		String url = BaseService.CRM_BASIC_URL;
		Collection<? extends Customers> collection = WebClient.create(url + "getNoAssociationCustomers")
				.accept(MediaType.APPLICATION_JSON).getCollection(Customers.class);
		return collection;
	}

	@Override
	public void assignedCustomerToDecidedZone(String id, String[] cids) {
		String url = BaseService.CRM_BASIC_URL;
		url = url + "assignedCustomerToDecidedZone/" + id;
		if (cids == null || cids.length == 0) {
			url = url + "/cids";
		} else {
			// StringBuffer stringBuffer = new StringBuffer();
			StringBuilder sb = new StringBuilder();
			for (String cid : cids) {
				sb.append(cid + ",");
			}
			String ids = sb.substring(0, sb.length() - 1);
			url = url + "/" + ids;
		}
		System.out.println(url);
		WebClient.create(url).put(null);
	}
}

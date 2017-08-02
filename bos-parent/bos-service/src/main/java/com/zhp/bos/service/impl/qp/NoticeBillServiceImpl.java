package com.zhp.bos.service.impl.qp;

import java.sql.Date;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhp.bos.dao.bc.DecidedzoneDao;
import com.zhp.bos.dao.bc.RegionDao;
import com.zhp.bos.dao.qp.NoticeBillDao;
import com.zhp.bos.dao.qp.WorkBillDao;
import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.bc.Region;
import com.zhp.bos.entity.bc.Subarea;
import com.zhp.bos.entity.customer.Customers;
import com.zhp.bos.entity.qp.NoticeBill;
import com.zhp.bos.entity.qp.WorkBill;
import com.zhp.bos.service.base.BaseService;
import com.zhp.bos.service.intf.qp.INoticeBillService;

@Service
@Transactional
public class NoticeBillServiceImpl implements INoticeBillService {
	@Autowired
	private NoticeBillDao noticeBillDao;
	@Autowired
	private DecidedzoneDao decidedzoneDao;
	@Autowired
	private WorkBillDao workBillDao;
	@Autowired
	private RegionDao regionDao;
	@Autowired
	private JmsTemplate JmsTemplate;

	@Override
	public Customers findCustomerByTelephone(NoticeBill model) {
		String url = BaseService.CRM_BASIC_URL;
		Customers customers = WebClient.create(url + "findCustomerByTelephone/" + model.getTelephone())
				.accept(MediaType.APPLICATION_JSON).get(Customers.class);
		return customers;
	}

	@Override
	public void save(NoticeBill model, String province, String city, String district) {
		final NoticeBill noticeBill = noticeBillDao.saveAndFlush(model);// 短信发送使用---详细地址（阿里大于字数限制，不允许全地址）+客户电话+客户姓名
		Boolean flag = false;
		model.setPickaddress(province + city + district + model.getPickaddress());
		String url = BaseService.CRM_BASIC_URL;
		// 1.自动下单
		// 1.1 地址完全匹配
		// 根据地址远程查询crm系统客户
		Customers customers1 = WebClient.create(url + "findCustomerByAddress/" + model.getPickaddress())
				.accept(MediaType.APPLICATION_JSON).get(Customers.class);
		// 如果客户存在
		if (customers1 != null) {
			// 如果客户已经被关联了定区
			flag = true;
			if (StringUtils.isNotBlank(customers1.getDecidedzoneId())) {
				// 根据定区id查询取派员并封装到model对象中
				String decidedzoneId = customers1.getDecidedzoneId();
				Decidedzone d = decidedzoneDao.findOne(decidedzoneId);
				createOrder(model, noticeBill, url, d);
				updateCustomersInfo(model, flag);
				return;
			}
		}
		// 1.2定区地址匹配
		// 根据省市区查询区域数据
		Region region = regionDao.findByProvinceAndCityAndDistrict(province, city, district);
		if (region != null) {
			// 获取区域对应的分区数据
			Set<Subarea> subareas = region.getSubareas();
			if (subareas != null && subareas.size() != 0) {
				for (Subarea subarea : subareas) {
					// 如果页面传过来的地址包含分区关键字，则这个分区就是所匹配的分区
					if (model.getPickaddress().contains(subarea.getAddresskey())) {
						Decidedzone d = subarea.getDecidedzone();
						// 业务单、工单生成 短信发送
						createOrder(model, noticeBill, url, d);
						updateCustomersInfo(model, flag);
						return;
					}
				}
			}
		}
		// 1.3自动分单失败
		model.setOrdertype("手动");
		// noticeBillDao.save(model);
		// 2.客户信息更新
		updateCustomersInfo(model, flag);
	}

	// 业务单、工单生成 短信发送方法集成
	private void createOrder(NoticeBill model, final NoticeBill noticeBill, String url, final Decidedzone d) {
		model.setStaff(d.getStaff());
		model.setOrdertype("自动");
		// 生成业务单
		// noticeBillDao.save(model);
		// 生成工单
		WorkBill workBill = new WorkBill();
		workBill.setAttachbilltimes(0);
		workBill.setBuildtime(new Date(System.currentTimeMillis()));
		workBill.setNoticeBill(model);
		workBill.setPickstate("新单");
		workBill.setRemark(model.getRemark());
		workBill.setStaff(d.getStaff());
		workBill.setType("新");
		workBillDao.save(workBill);
		// 下单成功之后，客户管理系统还要更行
		WebClient.create(url + "update/" + model.getCustomerId() + "/" + d.getId()).put(null);

		// 下单成功后发送短信 final String telephone = d.getStaff().getTelephone();
		JmsTemplate.send("bos_orderSuccess", new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				MapMessage message = session.createMapMessage();
				message.setString("staffTelephone", d.getStaff().getTelephone());// 取派员电话
				message.setString("costomerTelophone", noticeBill.getTelephone());// 客户电话
				message.setString("costomerName", noticeBill.getCustomerName());// 客户姓名
				message.setString("costomerAddress", noticeBill.getPickaddress());// 客户地址
				return message;
			}
		});

	}

	// 客户信息更改或录入
	public void updateCustomersInfo(NoticeBill model, Boolean flag) {
		String url = BaseService.CRM_BASIC_URL;
		if (model.getCustomerId() == null) {
			// 新客户:添加用户信息，----》地址分区匹配
			Customers c = new Customers();
			c.setAddress(model.getPickaddress());
			c.setName(model.getCustomerName());
			c.setTelephone(model.getTelephone());
			Response post = WebClient.create(url + "save/").accept(MediaType.APPLICATION_JSON).post(c);
			c = post.readEntity(Customers.class);
			model.setCustomerId(c.getId());
		} else {
			if (!flag) {
				// 老客户：并且地址发生了改变
				WebClient.create(url + "updateAddress/" + model.getCustomerId() + "/" + model.getPickaddress())
						.put(null);
			} else {
				// 老客户：但地址未变
			}
		}
	}
}

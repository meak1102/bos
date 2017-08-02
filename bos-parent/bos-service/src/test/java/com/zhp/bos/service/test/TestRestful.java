package com.zhp.bos.service.test;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.junit.Test;

import com.zhp.bos.entity.customer.Customers;

public class TestRestful {
	String basicUrl = "http://localhost:9090/boscrm/rs/customerService";

	@Test
	public void testRestful() {

		Customers customers = new Customers();
		customers.setName("小明同学");
		customers.setAddress("航头镇18号");
		customers.setTelephone("13412345678");
		customers.setStation("传智学院");
		WebClient.create(basicUrl + "/customers/save").post(customers);
	}

	@Test
	public void testRestful1() {
		Customers customers = WebClient.create(basicUrl + "/customers/getone/5").accept(MediaType.APPLICATION_JSON)
				.get(Customers.class);
		System.out.println(customers);
	}

	@Test
	public void testRestful2() {
		WebClient.create(basicUrl + "/customers/delete/10").delete();
	}

	@Test
	public void testRestful3() {
		WebClient.create(basicUrl + "/customers/update/5/DQ003").put(null);
	}

}

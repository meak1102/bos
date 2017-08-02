package com.zhp.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.qp.WorkBill;

public interface WorkBillDao extends JpaRepository<WorkBill, String>, JpaSpecificationExecutor<WorkBill> {
	@Modifying
	@Query("update WorkBill set type='销' where id=?1")
	void concelById(String id);
}

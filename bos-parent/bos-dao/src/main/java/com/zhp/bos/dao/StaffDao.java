package com.zhp.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.bc.Staff;

public interface StaffDao extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {
	@Modifying
	@Query("update Staff set deltag=0 where id =?1")
	void deltag(String id);

	@Modifying
	@Query("update Staff set deltag=1 where id =?1")
	void usedtag(String id);

	@Query("from Staff where telephone=?1")
	Staff checkTelephone(String telephone);

	@Query("from Staff where deltag=1")
	List<Staff> nameListInUse();

}

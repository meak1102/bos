package com.zhp.bos.dao.qp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhp.bos.entity.qp.NoticeBill;

public interface NoticeBillDao extends JpaRepository<NoticeBill, String>, JpaSpecificationExecutor<NoticeBill> {

}

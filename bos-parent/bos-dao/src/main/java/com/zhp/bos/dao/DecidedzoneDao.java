package com.zhp.bos.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.zhp.bos.entity.bc.Decidedzone;

public interface DecidedzoneDao extends JpaRepository<Decidedzone, String>, JpaSpecificationExecutor<Decidedzone> {

}

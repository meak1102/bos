package com.zhp.bos.dao.auth;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.auth.Function;

public interface FunctionDao extends JpaRepository<Function, String>, JpaSpecificationExecutor<Function> {
	@Query("from Function f inner join fetch f.roles  r where r.id=?1")
	List<Function> findFunctionByRoleId(String roleId);

}

package com.zhp.bos.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.zhp.bos.entity.bc.Decidedzone;
import com.zhp.bos.entity.bc.Subarea;

public interface SubareaDao extends JpaRepository<Subarea, String>, JpaSpecificationExecutor<Subarea> {
	@Query("from Subarea where decidedzone is null or decidedzone=?1")
	List<Subarea> subareaListInAssociation(Decidedzone decidedzone);

	@Modifying
	@Query("update Subarea set decidedzone=?2 where id=?1")
	void updateDecidedzone(String id, Decidedzone model);

	@Query("from Subarea where decidedzone =?1")
	List<Subarea> relationalSubarea(Decidedzone decidedzone);

	@Query("from Subarea where decidedzone is null")
	List<Subarea> subareaListInAssociation();

	@Modifying
	@Query("update Subarea set decidedzone=null where decidedzone=?1")
	void clearSubarea(Decidedzone model);

}

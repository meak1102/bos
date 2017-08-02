package com.zhp.bos.service.intf;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

import com.zhp.bos.entity.bc.Decidedzone;

public interface IDecidedzoneService {

	void save(Decidedzone model, String[] sids);

	Decidedzone checkdecidedzoneId(Decidedzone model);

	Page<Decidedzone> pageQuery(Specification<Decidedzone> specification, PageRequest pageRequest);

	void delete(String[] idsarr);

}

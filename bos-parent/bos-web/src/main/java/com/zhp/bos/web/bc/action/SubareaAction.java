package com.zhp.bos.web.bc.action;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.zhp.bos.entity.bc.Region;
import com.zhp.bos.entity.bc.Subarea;
import com.zhp.bos.utils.DownLoadUtils;
import com.zhp.bos.web.base.action.BaseAction;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class SubareaAction extends BaseAction<Subarea> {
	private File upload;

	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Action(value = "subareaAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_base_subarea") })
	public String save() {
		facadeService.getSubareaService().save(model);
		return "save";

	}

	/**
	 * 行内修改
	 * 
	 * @return
	 */
	@Action(value = "subareaAction_edit", results = { @Result(name = "edit", type = "json") })
	public String edit() {
		String regionId = (String) getParameter("region[id]");
		Region region = new Region();
		region.setId(regionId);
		model.setRegion(region);
		facadeService.getSubareaService().save(model);
		return "edit";

	}

	/**
	 * 查询未划分定区的分区以及指定定区的分区
	 * 
	 * @return
	 */
	@Action(value = "subareaAction_subareaListInAssociation", results = {
			@Result(name = "subareaListInAssociation", type = "fastJson", params = { "includesProperties", "did,sid,addresskey,position" }) })
	public String subareaListInAssociation() {
		String hiddenId = (String) getParameter("hiddenId");
		List<Subarea> list = facadeService.getSubareaService().subareaListInAssociation(hiddenId);
		push(list);
		return "subareaListInAssociation";

	}

	@Action(value = "subareaAction_oneClickUpload", results = { @Result(name = "oneClickUpload", type = "json") })
	public String oneClickUpload() {
		try {
			if (upload != null) {
				// 创建对工作薄文件的引用
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
				// 获取所有的的sheet
				int sheets = workbook.getNumberOfSheets();
				if (sheets != 0) {
					List<List<Subarea>> list = new ArrayList<>();
					for (int i = 0; i < sheets; i++) {
						HSSFSheet sheet = workbook.getSheetAt(i);
						List<Subarea> subareas = new ArrayList<>();
						for (Row row : sheet) {
							int rowNum = row.getRowNum();// 行号 从 0 开始
							if (rowNum == 0) {
								continue;
							}
							// 一行 = Region,表格数据封装导对象中
							Subarea subarea = new Subarea();
							// subarea.setId(row.getCell(0).getStringCellValue());
							// Decidedzone decidedzone = new Decidedzone();
							// subarea.setDecidedzone(decidedzone);
							// subarea.getDecidedzone().setId(row.getCell(1).getStringCellValue());
							Region region = new Region();
							subarea.setRegion(region);
							subarea.getRegion().setId(row.getCell(2).getStringCellValue());
							subarea.setAddresskey(row.getCell(3).getStringCellValue());
							subarea.setStartnum(row.getCell(4).getStringCellValue());
							subarea.setEndnum(row.getCell(5).getStringCellValue());
							subarea.setSingle(row.getCell(6).getStringCellValue().charAt(0));
							subarea.setPosition(row.getCell(7).getStringCellValue());
							subareas.add(subarea);
						}
						list.add(subareas);
					}
					facadeService.getSubareaService().oneCilckUpload(list);
				}
				push(true);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "oneClickUpload";
	}

	// 条件分页查询

	@Action(value = "subareaAction_pageQuery", results = {
			@Result(name = "pageQuery", type = "fastJson", params = { "root", "pageData", "excludesProperties", "decidedzone" }) })
	public String pageQuery() {
		Page<Subarea> pages = facadeService.getSubareaService().pageQuery(getSpecification(), getPageRequest());
		setPageData(pages);
		return "pageQuery";
	}
	// 条件查询数据导出

	@Action(value = "subareaAction_importData")
	public String importDat() {
		try {
			List<Subarea> datas = facadeService.getSubareaService().importDat(getSpecification());
			// 2: 下载 编写工作簿对象
			// workbook sheet row cell
			HSSFWorkbook book = new HSSFWorkbook();
			HSSFSheet sheet = book.createSheet("分区数据1");
			// excel标题
			HSSFRow first = sheet.createRow(0);
			first.createCell(0).setCellValue("分区编号");
			first.createCell(1).setCellValue("省");
			first.createCell(2).setCellValue("市");
			first.createCell(3).setCellValue("区");
			first.createCell(4).setCellValue("关键字");
			first.createCell(5).setCellValue("起始号");
			first.createCell(6).setCellValue("终止号");
			first.createCell(7).setCellValue("单双号");
			first.createCell(8).setCellValue("位置");
			// 数据体
			if (datas != null && datas.size() != 0) {
				for (Subarea s : datas) {
					// 循环一次创建一行
					int lastRowNum = sheet.getLastRowNum();// 获取当前excel最后一行行号
					HSSFRow row = sheet.createRow(lastRowNum + 1);
					row.createCell(0).setCellValue(s.getId());
					row.createCell(1).setCellValue(s.getRegion().getProvince());
					row.createCell(2).setCellValue(s.getRegion().getCity());
					row.createCell(3).setCellValue(s.getRegion().getDistrict());
					row.createCell(4).setCellValue(s.getAddresskey());
					row.createCell(5).setCellValue(s.getStartnum());
					row.createCell(6).setCellValue(s.getEndnum());
					row.createCell(7).setCellValue(s.getSingle() + "");
					row.createCell(8).setCellValue(s.getPosition());
				}
				// 第一个sheet数据完成
			}
			String filename = "分区数据.xls";
			HttpServletResponse response = getResponse();
			HttpServletRequest request = getRequest();
			response.setHeader("Content-Disposition",
					"attachment;filename=" + DownLoadUtils.getAttachmentFileName(filename, request.getHeader("user-agent")));
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			book.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "none";
	}

	// 区域删除
	@Action(value = "subareaAction_delete", results = { @Result(name = "delete", type = "json") })

	public String delete() {
		String idString = (String) getParameter("ids");
		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");
				facadeService.getSubareaService().delete(idsarr);
				push(true);
			} else {
				push(false);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "delete";
	}

	// 定区行双击时根据定区id查询其对应的分区数据
	@Action(value = "subareaAction_relationalSubarea", results = {
			@Result(name = "relationalSubarea", type = "fastJson", params = { "excludesProperties", "decidedzone" }) })
	public String relationalSubarea() {
		List<Subarea> list = facadeService.getSubareaService().relationalSubarea(model);
		push(list);
		return "relationalSubarea";
	}

	// 查询条件封装
	public Specification<Subarea> getSpecification() {
		return new Specification<Subarea>() {
			@Override
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> list = new ArrayList<Predicate>();
				if (StringUtils.isNotBlank(model.getAddresskey())) {
					list.add(cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%"));
				}
				Join<Subarea, Region> join = root.join(root.getModel().getSingularAttribute("region", Region.class), JoinType.LEFT);
				if (model.getRegion() != null && StringUtils.isNotBlank(model.getRegion().getProvince())) {

					list.add(cb.like(join.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%"));
				}
				if (model.getRegion() != null && StringUtils.isNotBlank(model.getRegion().getCity())) {
					list.add(cb.like(join.get("city").as(String.class), "%" + model.getRegion().getCity() + "%"));

				}
				if (model.getRegion() != null && StringUtils.isNotBlank(model.getRegion().getDistrict())) {
					list.add(cb.like(join.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%"));

				}
				/*
				 * Join<Subarea, Decidedzone> join2 = root
				 * .join(root.getModel().getSingularAttribute("decidedzone",
				 * Decidedzone.class), JoinType.LEFT); if
				 * (model.getDecidedzone() != null &&
				 * StringUtils.isNotBlank(model.getDecidedzone().getId())) {
				 * list.add(cb.equal(join2.get("id").as(String.class),
				 * model.getDecidedzone().getId())); }
				 */
				// 比较对象,比较OID，OID相同则对象相同
				if (model.getDecidedzone() != null && StringUtils.isNotBlank(model.getDecidedzone().getId())) {
					list.add(cb.equal(root.get("decidedzone").as(String.class), model.getDecidedzone()));
				}
				Predicate[] p = new Predicate[list.size()];
				return cb.and(list.toArray(p));
			}
		};
	}

}

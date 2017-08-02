package com.zhp.bos.web.bc.action;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;
import com.zhp.bos.entity.bc.Region;
import com.zhp.bos.utils.DownLoadUtils;
import com.zhp.bos.utils.PinYin4jUtils;
import com.zhp.bos.web.base.action.BaseAction;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

@Controller
@Scope("prototype")
@Namespace("/")
@ParentPackage("bosManager")
public class RegionAction extends BaseAction<Region> {
	/**
	 * 区域添加或修改
	 * 
	 * @return
	 */
	@Action(value = "regionAction_save", results = { @Result(name = "save", type = "redirectAction", location = "page_base_region") })
	public String save() {
		facadeService.getRegionService().save(model);
		return "save";

	}

	/**
	 * 一键上传
	 * 
	 * @return
	 */
	private File upload;

	public void setUpload(File upload) {
		this.upload = upload;
	}

	@Action(value = "regionAction_oneClickUpload", results = { @Result(name = "oneClickUpload", type = "json") })
	public String oneClickUpload() {
		try {
			if (upload != null) {
				// 创建对工作薄文件的引用
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(upload));
				// 获取所有的的sheet
				int sheets = workbook.getNumberOfSheets();
				if (sheets != 0) {
					List<List<Region>> list = new ArrayList<>();
					for (int i = 0; i < sheets; i++) {
						HSSFSheet sheet = workbook.getSheetAt(i);
						List<Region> regions = new ArrayList<>();
						for (Row row : sheet) {
							int rowNum = row.getRowNum();// 行号 从 0 开始
							if (rowNum == 0) {
								continue;
							}
							// 一行 = Region,表格数据封装导对象中
							Region region = new Region();
							region.setId(row.getCell(0).getStringCellValue());
							String province = row.getCell(1).getStringCellValue();
							region.setProvince(province);
							String city = row.getCell(2).getStringCellValue();
							region.setCity(city);
							String district = row.getCell(3).getStringCellValue();
							region.setDistrict(district);
							city = city.substring(0, city.length() - 1);
							province = province.substring(0, province.length() - 1);
							district = district.substring(0, district.length() - 1);
							// 邮编
							region.setPostcode(row.getCell(4).getStringCellValue());
							// 城市编码
							region.setCitycode(PinYin4jUtils.hanziToPinyin(city, ""));
							String[] strs = PinYin4jUtils.getHeadByString(province + city + district);
							// 省市区简码
							region.setShortcode(PinYin4jUtils.stringArrayToString(strs));
							regions.add(region);

						}
						list.add(regions);
					}
					facadeService.getRegionService().oneCilckUpload(list);
				}
				push(true);
			}
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "oneClickUpload";
	}

	// 无条件分页查询
	@Action(value = "regionAction_pageQuery")
	public String pageQuery() {
		Page<Region> pages = null;
		Object tag = getParameter("tag");// 前台标志，存在则为条件查询
		if (tag != null) {
			// 条件封装
			Specification<Region> spec = new Specification<Region>() {
				@Override
				public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
					List<Predicate> list = new ArrayList<>();
					if (StringUtils.isNotBlank(model.getProvince())) {
						list.add(cb.like(root.get("province").as(String.class), "%" + model.getProvince() + "%"));
					}
					if (StringUtils.isNotBlank(model.getCity())) {
						list.add(cb.like(root.get("city").as(String.class), "%" + model.getCity() + "%"));
					}
					if (StringUtils.isNotBlank(model.getProvince())) {
						list.add(cb.like(root.get("district").as(String.class), "%" + model.getDistrict() + "%"));
					}
					if (StringUtils.isNotBlank(model.getPostcode())) {
						list.add(cb.equal(root.get("postcode").as(String.class), model.getPostcode()));
					}
					if (StringUtils.isNotBlank(model.getShortcode())) {
						list.add(cb.like(root.get("shortcode").as(String.class), "%" + model.getShortcode() + "%"));
					}
					if (StringUtils.isNotBlank(model.getCitycode())) {
						list.add(cb.like(root.get("citycode").as(String.class), "%" + model.getCitycode() + "%"));
					}
					Predicate[] predicates = new Predicate[list.size()];
					return cb.and(list.toArray(predicates));
				}
			};
			pages = facadeService.getRegionService().pageQuery(spec, getPageRequest());
			setPageData(pages);
			return "pageQuery";

		} else {
			String jsonString = (String) facadeService.getRegionService().NoPredicaTePageQuery(getPageRequest());
			HttpServletResponse response = getResponse();
			response.setContentType("text/json;charset=utf-8");
			try {
				response.getWriter().println(jsonString);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return NONE;
		}

	}

	// 区域删除
	@Action(value = "regionAction_delete", results = { @Result(name = "delete", type = "json") })
	public String delete() {
		String idString = (String) getParameter("ids");
		try {
			if (StringUtils.isNotBlank(idString)) {
				String[] idsarr = idString.split(",");
				facadeService.getRegionService().delete(idsarr);
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

	// 区域查询
	@Action(value = "regionAction_regionList", results = {
			@Result(name = "regionList", type = "fastJson", params = { "includesProperties", "id,region" }) })
	public String regionList() {
		String q = (String) getParameter("q");
		List<Region> list = facadeService.getRegionService().regionList(q);
		/*
		 * List<Map<String, String>> regionList = new ArrayList<>(); if (list !=
		 * null || list.size() != 0) { for (Region region : list) { Map<String,
		 * String> map = new HashMap<>(); map.put("id", region.getId());
		 * map.put("region", region.getProvince() + region.getCity() +
		 * region.getDistrict()); regionList.add(map); } }
		 */
		push(list);
		return "regionList";
	}

	@Autowired
	DataSource dataSource;

	// jasperReport报表导出
	@Action(value = "regionAction_doJasExport")
	public String doJasExport() {
		try {
			// 1: 加载设计文件 report2.jrxml
			String path = ServletActionContext.getServletContext().getRealPath("/jr/heima04_.jrxml");
			// 2: 报表 parameter 赋值 需要Map 集合
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("company", "传智播客");
			// 3: 编译该文件 JasperCompilerManager
			JasperReport report = JasperCompileManager.compileReport(path);
			// 4: JapserPrint =
			// JasperFillManager.fillReport(report,map,connection)
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, dataSource.getConnection());
			// 5: 下载 准备一个流 两个头
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream outputStream = response.getOutputStream();
			String filename = "工作单报表.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition", "attachment;filename="
					+ DownLoadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			// 6: JapdfExport 定义报表输出源
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// 7: 导出
			exporter.exportReport();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;

	}

	// Itext报表pdf导出
	@Action(value = "regionAction_doItextExport")
	public String doItextExport() {
		// 1: 数据库最新的数据
		List<Region> regions = facadeService.getRegionService().findAll();
		// 2: 生成pdf --->Document 对象 具有一定格式数据 ... Table 对象 填充到Document对象中
		// Table 行和列
		// itext 报表 下载
		try {
			Document document = new Document();
			// response
			HttpServletResponse response = getResponse();
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			// pdf 可以设置密码
			writer.setEncryption("itcast".getBytes(), "zhp".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
			// 浏览器下载 ...两个头
			String filename = new Date(System.currentTimeMillis()).toLocaleString() + "_区域数据.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));// mime
																									// 类型
			response.setHeader("Content-Disposition", "attachment;filename="
					+ DownLoadUtils.getAttachmentFileName(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			// 打开文档
			document.open();
			Table table = new Table(5, regions.size() + 1);// 5列 行号 0 开始
			// table.setBorderWidth(1f);
			// table.setAlignment(1);// // 其中1为居中对齐，2为右对齐，3为左对齐
			// table.setBorder(1); // 边框
			// table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			// // 水平对齐方式
			// table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			// // 垂直对齐方式
			// 设置表格字体
			BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
			// 表头
			table.addCell(buildCell("省", font));
			table.addCell(buildCell("市", font));
			table.addCell(buildCell("区", font));
			table.addCell(buildCell("邮政编码", font));
			table.addCell(buildCell("简码", font));

			// 表格数据
			for (Region region : regions) {
				table.addCell(buildCell(region.getProvince(), font));
				table.addCell(buildCell(region.getCity(), font));
				table.addCell(buildCell(region.getDistrict(), font));
				table.addCell(buildCell(region.getPostcode(), font));
				table.addCell(buildCell(region.getShortcode(), font));
			}
			// 向文档添加表格
			document.add(table);
			document.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return NONE;// 称

	}

	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		Cell cell = new Cell(phrase);
		// 设置垂直居中
		// cell.setVerticalAlignment(1);
		// 设置水平居中
		// cell.setHorizontalAlignment(1);
		return cell;
	}

}

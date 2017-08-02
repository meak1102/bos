package com.zhp.bos.entity.qp;
// Generated 2017-7-27 15:38:12 by Hibernate Tools 3.2.2.GA


import com.zhp.bos.entity.bc.Staff;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 * WorkBill generated by hbm2java
 */
@Entity
@Table(name="qp_workbill"
    ,catalog="bos"
)
public class WorkBill  implements java.io.Serializable {


     private String id;
     private NoticeBill noticeBill;
     private Staff staff;// 取派员编号
 	private String type;// 工单类型 新,追,销
 	private String pickstate;// 取件状态 新单:没有确认货物状态的
 	// 已通知:自动下单下发短信
 	// 已确认:接到短信,回复收信确认信息
 	// 已取件:已经取件成功,发回确认信息生成工作单
 	// 已取消:销单
 	private Date buildtime;// 工单生成时间
 	private Integer attachbilltimes;// 追单次数
 	private String remark;// 备注

    public WorkBill() {
    }

	
    public WorkBill(Date buildtime) {
        this.buildtime = buildtime;
    }
    public WorkBill(NoticeBill noticeBill, Staff staff, String type, String pickstate, Date buildtime, Integer attachbilltimes, String remark) {
       this.noticeBill = noticeBill;
       this.staff = staff;
       this.type = type;
       this.pickstate = pickstate;
       this.buildtime = buildtime;
       this.attachbilltimes = attachbilltimes;
       this.remark = remark;
    }
   
     @GenericGenerator(name="generator", strategy="uuid")@Id @GeneratedValue(generator="generator")
    
    @Column(name="ID", unique=true, nullable=false, length=32)
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="NOTICEBILL_ID")
    public NoticeBill getNoticeBill() {
        return this.noticeBill;
    }
    
    public void setNoticeBill(NoticeBill noticeBill) {
        this.noticeBill = noticeBill;
    }
@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="STAFF_ID")
    public Staff getStaff() {
        return this.staff;
    }
    
    public void setStaff(Staff staff) {
        this.staff = staff;
    }
    
    @Column(name="TYPE", length=20)
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    @Column(name="PICKSTATE", length=20)
    public String getPickstate() {
        return this.pickstate;
    }
    
    public void setPickstate(String pickstate) {
        this.pickstate = pickstate;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="BUILDTIME", nullable=false, length=0)
    public Date getBuildtime() {
        return this.buildtime;
    }
    
    public void setBuildtime(Date buildtime) {
        this.buildtime = buildtime;
    }
    
    @Column(name="ATTACHBILLTIMES", precision=8, scale=0)
    public Integer getAttachbilltimes() {
        return this.attachbilltimes;
    }
    
    public void setAttachbilltimes(Integer attachbilltimes) {
        this.attachbilltimes = attachbilltimes;
    }
    
    @Column(name="REMARK")
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }




}



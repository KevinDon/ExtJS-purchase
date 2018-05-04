package com.newaim.purchase.flow.workflow.entity;

import com.newaim.core.utils.SessionUtils;
import com.newaim.purchase.admin.account.vo.UserVo;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Mark on 2017/10/10.
 */
@Entity
@Table(name = "na_flow_operator_history")
public class FlowOperatorHistory implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "FOH")})
    private String id;
    private String businessId;
    private String operatorId;
    private String operatorCnName;
    private String operatorEnName;
    private Integer type;
    private Date createdAt;
    private String remark;
    private String data;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getOperatorCnName() {
        return operatorCnName;
    }

    public void setOperatorCnName(String operatorCnName) {
        this.operatorCnName = operatorCnName;
    }

    public String getOperatorEnName() {
        return operatorEnName;
    }

    public void setOperatorEnName(String operatorEnName) {
        this.operatorEnName = operatorEnName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Transient
    public String getOperatorName() {
        UserVo user = SessionUtils.currentUserVo();

        if(user.getLang().equals("zh_CN")){
            return  this.getOperatorCnName();
        }else if(user.getLang().equals("en_AU")){
            return this.getOperatorEnName();
        }
        return null;
    }
}

package com.newaim.purchase.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.newaim.purchase.archives.product.vo.ProductVo;

import java.io.Serializable;

/**
 *
 * @author Mark
 * @date 2017/12/22
 * @moidfy by lance at 20180129
 */
public class OmsOrderDetailDto implements Serializable {

    @JsonProperty("order_no")
    private String orderNo;

    @JsonProperty("job_no")
    private String jobNo;

    private String tid;

    private String sku;

    private Integer num;

    @JsonProperty("lock_num")
    private Integer lockNum;

    private ProductVo product;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getJobNo() {
        return jobNo;
    }

    public void setJobNo(String jobNo) {
        this.jobNo = jobNo;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getLockNum() {
        return lockNum;
    }

    public void setLockNum(Integer lockNum) {
        this.lockNum = lockNum;
    }

    public ProductVo getProduct() {
        return product;
    }

    public void setProduct(ProductVo product) {
        this.product = product;
    }

}

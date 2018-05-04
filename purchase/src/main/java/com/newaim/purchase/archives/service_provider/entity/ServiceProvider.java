package com.newaim.purchase.archives.service_provider.entity;

import com.newaim.core.utils.SessionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "na_vendor")
public class ServiceProvider implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "SP")})
    private String id;
    private String code;
    private String cnName;
    private String enName;
    private String categoryId;
    private String categoryCnName;
    private String categoryEnName;

    private String buyerId;
    private String buyerCnName;
    private String buyerEnName;

    private String director;

    private String address;

    private String postcode;

    private String abn;

    private String website;

    private String rating;

    private String photos;

    private String files;

    private Integer source;

    private Integer currency;

    private String paymentProvision;

    private String dynContent;

    private String dynFields1;

    private String dynFields2;

    private Integer status;

    private String creatorId;
    private String creatorCnName;
    private String creatorEnName;

    private Date createdAt;

    private Date updatedAt;

    private String departmentId;

    private String departmentCnName;
    private String departmentEnName;

    private Integer type;

    @Transient
    private String images;

    private String originPort;

    private String destinationPort;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getCurrency() {
        return currency;
    }

    public void setCurrency(Integer currency) {
        this.currency = currency;
    }

    public String getPaymentProvision() {
        return paymentProvision;
    }

    public void setPaymentProvision(String paymentProvision) {
        this.paymentProvision = paymentProvision;
    }

    public String getDynContent() {
        return dynContent;
    }

    public void setDynContent(String dynContent) {
        this.dynContent = dynContent;
    }

    public String getDynFields1() {
        return dynFields1;
    }

    public void setDynFields1(String dynFields1) {
        this.dynFields1 = dynFields1;
    }

    public String getDynFields2() {
        return dynFields2;
    }

    public void setDynFields2(String dynFields2) {
        this.dynFields2 = dynFields2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(String buyerId) {
        this.buyerId = buyerId;
    }

    public String getBuyerCnName() {
        return buyerCnName;
    }

    public void setBuyerCnName(String buyerCnName) {
        this.buyerCnName = buyerCnName;
    }

    public String getBuyerEnName() {
        return buyerEnName;
    }

    public void setBuyerEnName(String buyerEnName) {
        this.buyerEnName = buyerEnName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryCnName() {
        return categoryCnName;
    }

    public void setCategoryCnName(String categoryCnName) {
        this.categoryCnName = categoryCnName;
    }

    public String getCategoryEnName() {
        return categoryEnName;
    }

    public void setCategoryEnName(String categoryEnName) {
        this.categoryEnName = categoryEnName;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorCnName() {
        return creatorCnName;
    }

    public void setCreatorCnName(String creatorCnName) {
        this.creatorCnName = creatorCnName;
    }

    public String getCreatorEnName() {
        return creatorEnName;
    }

    public void setCreatorEnName(String creatorEnName) {
        this.creatorEnName = creatorEnName;
    }


    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCnName() {
        return departmentCnName;
    }

    public void setDepartmentCnName(String departmentCnName) {
        this.departmentCnName = departmentCnName;
    }

    public String getDepartmentEnName() {
        return departmentEnName;
    }

    public void setDepartmentEnName(String departmentEnName) {
        this.departmentEnName = departmentEnName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getOriginPort() {
        return originPort;
    }

    public void setOriginPort(String originPort) {
        this.originPort = originPort;
    }

    public String getDestinationPort() {
        return destinationPort;
    }

    public void setDestinationPort(String destinationPort) {
        this.destinationPort = destinationPort;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    /**
     * 返回所有起始港口
     * @return
     */
    @Transient
    public Integer[] getOriginPorts(){
        String[] data = StringUtils.split(this.originPort, ",");
        return convertStringArrayToIntegerArray(data);
    }

    /**
     * 返回所有目的港口
     * @return
     */
    @Transient
    public Integer[] getDestinationPorts(){
        String[] data = StringUtils.split(this.destinationPort, ",");
        return convertStringArrayToIntegerArray(data);
    }

    /**
     * 字符数组转为整形数组
     */
    private Integer[] convertStringArrayToIntegerArray(String[] data){
        if(data != null){
            Integer[] result = new Integer[data.length];
            for (int i = 0; i < data.length; i++) {
                result[i] = Integer.valueOf(data[i]);
            }
            return result;
        }
        return null;
    }

    @Transient
    public String getName() {
        if(SessionUtils.isCnLang()){
            return  this.cnName;
        }else if(SessionUtils.isEnLang()){
            return this.enName;
        }
        return null;
    }

    @Transient
    public String getCategoryName() {
        if(SessionUtils.isCnLang()){
            return  this.categoryCnName;
        }else if(SessionUtils.isEnLang()){
            return this.categoryEnName;
        }
        return null;
    }


    @Transient
    public String getBuyerName() {
        if(SessionUtils.isCnLang()){
            return  this.buyerCnName;
        }else if(SessionUtils.isEnLang()){
            return this.buyerEnName;
        }
        return null;
    }

    @Transient
    public String getCreatorName() {
        if(SessionUtils.isCnLang()){
            return  this.creatorCnName;
        }else if(SessionUtils.isEnLang()){
            return this.creatorEnName;
        }
        return null;
    }

    @Transient
    public String getDepartmentName() {
        if(SessionUtils.isCnLang()){
            return  this.departmentCnName;
        }else if(SessionUtils.isEnLang()){
            return this.departmentEnName;
        }
        return null;
    }

}

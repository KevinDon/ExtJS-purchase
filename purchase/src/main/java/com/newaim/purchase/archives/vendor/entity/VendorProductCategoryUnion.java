package com.newaim.purchase.archives.vendor.entity;

import com.newaim.purchase.archives.product.entity.ProductCategory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Mark on 2017/9/15.
 */
@Entity
@Table(name = "na_vendor_product_category")
public class VendorProductCategoryUnion implements Serializable{

    @Id
    @GeneratedValue(generator = "idGenerator")
    @GenericGenerator(name = "idGenerator", strategy = "com.newaim.core.jpa.IdGenerator",
            parameters = {@org.hibernate.annotations.Parameter(name = "prefix", value = "VDPCU")})
    private String id;
    @Column(name = "vendor_id")
    private String vendorId;
    @Column(name = "product_category_id")
    private String productCategoryId;
    private String alias;
    private Integer orderIndex;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "product_category_id", insertable=false, updatable=false)
    @NotFound(action=NotFoundAction.IGNORE)
    private ProductCategory productCategory;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getProductCategoryId() {
		return productCategoryId;
	}

	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Integer getOrderIndex() {
		return orderIndex;
	}

	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	public ProductCategory getProductCategory() {
		return productCategory;
	}

	public void setProductCategory(ProductCategory productCategory) {
		this.productCategory = productCategory;
	}


}

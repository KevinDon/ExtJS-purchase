package com.newaim.purchase.archives.product.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.archives.product.entity.Product;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductDao extends BaseDao<Product, String>, ProductDaoCustom {

    /**
     * 通过Sku查找产品
     * @param sku
     * @return
     */
    @Query("select t from Product t where t.sku = ?1")
    List<Product> findProductsBySku(String sku);

    /**
     * 通过barcode查找产品
     * @param barcode
     * @return
     */
    @Query("select t from Product t where t.barcode = ?1")
    List<Product> findProductsByBarcode(String barcode);

    /**
     * 通过ean查找产品
     * @param ean
     * @return
     */
    @Query("select t from Product t where t.ean = ?1")
    List<Product> findProductsByEan(String ean);

    @Query("select t from Product t where (t.isSync = 1 or t.isSync = 2) and t.status = 1")
    List<Product> findAllSycnProducts();

    Product findProductById( String id);

    Product findProductBySku (String sku);

    @Query(nativeQuery = true, value = "select DISTINCT t.sku from na_product t where t.barcode is null")
    List<String> findAllSkusByEmptyBarcode();

    @Modifying
    @Query(nativeQuery = true, value = "update na_product set barcode = :barcode where sku = :sku")
    void updateProductBarcode(@Param("sku") String sku, @Param("barcode") String barcode);


    @Query("select p from Product p where p.prop.hsCode = :hsCode")
    List<Product> findProductsByHsCode(@Param("hsCode") String hsCode);

}

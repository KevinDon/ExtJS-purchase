package com.newaim.purchase.admin.system.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.system.entity.AutoCode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AutoCodeDao extends BaseDao<AutoCode, String> {

    @Query("select t from AutoCode t where t.status = 1 and code = :code and t.departmentId = :departmentId order by id desc")
    List<AutoCode> findByCodeAndDepartmentId(@Param("code") String code, @Param("departmentId") String departmentId);

    @Query("select t from AutoCode t where t.status = 1 and code = :code order by id desc")
    List<AutoCode> findByCode(@Param("code") String code);
}

package com.newaim.purchase.admin.account.dao;

import com.newaim.core.jpa.BaseDao;
import com.newaim.purchase.admin.account.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Mark on 2017/8/10.
 */
@Repository
public interface UserDao extends BaseDao<User, String> {

    /**
     * 通过账号查找用户信息
     * @param account 用户账号
     * @return 用户信息
     */
    User findUserByAccount(String account);

    List<User> findUsersByAccountInAndStatusEquals(List<String> accounts, Integer status);

    /**
     * 通过当前部门id向上查找拥有指定角色的人
     *
     * @param roleCodes 角色code集合
     * @param departmentId 当前部门id
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT u.* from na_account_user u, na_account_user_role ur,na_account_role r \n" +
            "where u.id = ur.user_id and ur.role_id = r.id and u.status = 1 and r.status = 1  and  r.code in :roleCodes \n" +
            "and exists( \n" +
            "  with RECURSIVE t as ( \n" +
            "    select d.id, d.parent_id from na_account_department d where d.id = :departmentId \n" +
            "    UNION ALL SELECT d.id, d.parent_id  from na_account_department d, t where d.id = t.parent_id) \n" +
            "  select id from t where t.id = u.department_id\n" +
            ")")
    List<User> findUpUserByRoleCodeAndDepartmentId(@Param("roleCodes") List<String> roleCodes, @Param("departmentId") String departmentId);

    /**
     * 通过当前部门id向下查找拥有指定角色的人
     *
     * @param roleCodes 角色code集合
     * @param departmentId 当前部门id
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT u.* from na_account_user u, na_account_user_role ur,na_account_role r \n" +
            "where u.id = ur.user_id and ur.role_id = r.id and u.status = 1 and r.status = 1  and  r.code in :roleCodes \n" +
            "and exists( \n" +
            "  with RECURSIVE t as ( \n" +
            "    select d.id, d.parent_id from na_account_department d where d.id = :departmentId \n" +
            "    UNION ALL SELECT d.id, d.parent_id  from na_account_department d, t where d.parent_id = t.id) \n" +
            "  select id from t where t.id = u.department_id\n" +
            ")")
    List<User> findDownUserByRoleCodeAndDepartmentId(@Param("roleCodes") List<String> roleCodes, @Param("departmentId") String departmentId);


    /**
     * 通过角色查找用户
     * @param roleCode 角色code
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT u.* from na_account_user u, na_account_user_role ur,na_account_role r \n" +
            "where u.id = ur.user_id and ur.role_id = r.id and u.status = 1 and r.status = 1  and  r.code = :roleCode \n")
    List<User> findUserByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 通过角色查找用户
     * @param roleCodes 角色code集合
     * @return
     */
    @Query(nativeQuery = true, value = "SELECT u.* from na_account_user u, na_account_user_role ur,na_account_role r \n" +
            "where u.id = ur.user_id and ur.role_id = r.id and u.status = 1 and r.status = 1  and  r.code in :roleCodes \n")
    List<User> findUserByRoleCodes(@Param("roleCodes") List<String> roleCodes);

    /**
     * 查找指定部门id下所有用户
     * @param departmentId
     * @return
     */
    List<User> findByDepartmentId(String departmentId);
}

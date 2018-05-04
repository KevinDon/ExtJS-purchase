package com.newaim.core.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;

/**
 * Created by Mark on 2017/9/20.
 */
@NoRepositoryBean
public interface BaseDao<T, ID extends Serializable> extends JpaRepository<T, String>, JpaSpecificationExecutor<T> {

    /**
     * 清除事务中缓存
     */
    void clear();
}

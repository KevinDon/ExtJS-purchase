package com.newaim.core.jpa;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

import java.util.List;
import java.util.Map;

/**
 * Created by Mark on 2017/9/20.
 */
public class BaseDaoCustomImpl {

    protected static Log logger = LogFactory.getLog(BaseDaoCustomImpl.class);

    @Autowired
    private EntityManager entityManager;

    public Session getSession(){
        //获取hibernate session
        return entityManager.unwrap(Session.class);
    }

    public void clear(){
        entityManager.clear();
    }

    /**
     * @param params 查询参数
     */
    @SuppressWarnings("unchecked")
	public <T> List<T> list( String hql, Object params){
        Query query = getSession().createQuery(hql);
        query.setProperties(params);
        return query.list();
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> list(String hql, Map<String, Object> params){
        Query query = getSession().createQuery(hql);
        query.setProperties(params);
        return query.list();
    }

    public <T> List<T> list(String hql, Map<String, Object> params, int maxResults){
        Query query = getSession().createQuery(hql);
        query.setProperties(params);
        query.setMaxResults(maxResults);
        return query.list();
    }

    @SuppressWarnings("unchecked")
	public <T> List<T> listBySql(String sql, Map<String, Object> params){
    	SQLQuery query = getSession().createSQLQuery(sql);
    	query.setProperties(params);
    	return query.list();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> listBySql(String sql, Map<String, Object> params, Class<T> clazz){
        SQLQuery query = getSession().createSQLQuery(sql);
        query.setProperties(params);
        query.addEntity(clazz);
        return query.list();
    }

    public int execute(String hql, Map<String, Object> params){
	    Query query = getSession().createQuery(hql);
	    query.setProperties(params);

	    return query.executeUpdate();
    }

    public int executeBySql(String sql, Map<String, Object> params){
        Query query = getSession().createSQLQuery(sql);
        query.setProperties(params);

        return query.executeUpdate();
    }

}

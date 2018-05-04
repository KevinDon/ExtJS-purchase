package com.newaim.purchase.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.google.common.collect.Maps;
import com.newaim.purchase.admin.account.service.ShiroDbRealm;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.AnonymousFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 * Created by Mark on 2017/7/24.
 */
@Configuration
public class ShiroConfig {
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter"));
        filterRegistration.setEnabled(true);
        filterRegistration.addUrlPatterns("/*");
        filterRegistration.setDispatcherTypes(DispatcherType.REQUEST);
        return filterRegistration;
    }

    /**
     * @see ShiroFilterFactoryBean
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(){
        ShiroFilterFactoryBean bean = new ShiroFilterFactoryBean();
        bean.setSecurityManager(securityManager());
        bean.setLoginUrl("/login");
        bean.setUnauthorizedUrl("/unauthor");
        Map<String, Filter> filters = Maps.newHashMap();
        filters.put("anon", new AnonymousFilter());
        bean.setFilters(filters);

        Map<String, String> chains = Maps.newHashMap();
        chains.put("/js/view/admin/**", "authc");
        chains.put("/js/view/archives/**", "authc");
        chains.put("/login", "anon");
        chains.put("/logout", "anon");
        chains.put("/js/lib/**", "anon");
        chains.put("/js/view/core/**", "anon");
        chains.put("/js/view/locale/**", "anon");
        chains.put("/css/**", "anon");
        chains.put("/fonts/**", "anon");
        chains.put("/var/upload/**", "anon");
//        chains.put("/js/lib/ext/bootstrap.js", "anon");
//        chains.put("/js/lib/ext/ext-all.js", "anon");
//        chains.put("/js/lib/ext/locale/ext-lang-zh_CN.js", "anon");
//        chains.put("/js/lib/ext/locale/ext-lang-en_AU.js", "anon");
//        chains.put("/js/lib/ext/*", "anon");
//        chains.put("/js/lib/locale/zh_CN.js", "anon");
//        chains.put("/js/lib/locale/en_AU.js", "anon");
//        chains.put("/js/lib/jquery/jquery.min.js", "anon");
//        chains.put("/js/view/core/AppUtil.js", "anon");
//        chains.put("/js/view/App.js", "anon");
//        chains.put("/js/view/App.LoginWin.js", "anon");
//        chains.put("/js/view/IndexPage.js", "anon");
//        chains.put("/js/lib/ext/resources/css/ext-all.css", "anon");
//        chains.put("/js/lib/ext/resources/ext-theme-classic/ext-theme-classic-all.css", "anon");
//        chains.put("/css/font-awesome.css", "anon");
//        chains.put("/css/loading.css", "anon");
//        chains.put("/css/style.css", "anon");
//        chains.put("/images/login-icon.png", "anon");
//        chains.put("/images/login-reset.png", "anon");
        
        bean.setFilterChainDefinitionMap(chains);
        return bean;
    }



    /**
     * @see org.apache.shiro.mgt.SecurityManager
     * @return
     */
    @Bean(name="securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(shiroDbRealm());
        manager.setCacheManager(shiroCacheManager());
        manager.setSessionManager(defaultWebSessionManager());
        return manager;
    }

    /**
     * @see DefaultWebSessionManager
     * @return
     */
    @Bean(name="sessionManager")
    public DefaultWebSessionManager defaultWebSessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setCacheManager(shiroCacheManager());
        sessionManager.setGlobalSessionTimeout(1800000);
        sessionManager.setDeleteInvalidSessions(true);
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setDeleteInvalidSessions(true);
        return sessionManager;
    }

    @Bean
    @DependsOn(value="lifecycleBeanPostProcessor")
    public ShiroDbRealm shiroDbRealm() {
        ShiroDbRealm shiroDbRealm = new ShiroDbRealm();
        shiroDbRealm.setCacheManager(shiroCacheManager());
        return shiroDbRealm;
    }

    @Bean
    public EhCacheManager shiroCacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean(name = "shiroDialect")
    public ShiroDialect shiroDialect(){
        return new ShiroDialect();
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAAP = new DefaultAdvisorAutoProxyCreator();
        defaultAAP.setProxyTargetClass(true);
        return defaultAAP;
    }

    /**
     * AuthorizationAttributeSourceAdvisor，shiro里实现的Advisor类，
     * 内部使用AopAllianceAnnotationsAuthorizingMethodInterceptor来拦截用以下注解的方法。
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
        AuthorizationAttributeSourceAdvisor aASA = new AuthorizationAttributeSourceAdvisor();
        aASA.setSecurityManager(securityManager());
        return aASA;
    }

}

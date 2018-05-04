package com.newaim.core.utils;

import com.newaim.purchase.admin.account.vo.UserVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.io.Serializable;


/**
 * Created by Mark on 2017/10/10.
 */
public class SessionUtils  implements Serializable {

    public static final String LANG_CN = "zh_CN";
    public static final String LANG_EN = "en_AU";

    /**
     * 获取当前登陆用户
     * @return
     */
    public static UserVo currentUserVo(){
        Subject subject = SecurityUtils.getSubject();
        return  (UserVo) subject.getPrincipal();
    }

    /**
     * 获取当前用户语言环境
     * @return
     */
    public static String getUserLang(){
        return currentUserVo().getLang();
    }

    /**
     * 判断当前用户语言环境是否为中文
     */
    public static boolean isCnLang(){
        return LANG_CN.equals(getUserLang());
    }

    /**
     * 判断当前用户语言环境是否为英文
     */
    public static boolean isEnLang(){
        return LANG_EN.equals(getUserLang());
    }
}

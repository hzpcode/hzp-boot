package com.hzp.web.model;


import com.hzp.web.constant.AccountType;

import java.util.List;

/**
 * 账号
 *
 * @author wangchun
 * @date 2020/8/12
 */
public interface Account {

    /**
     * 账号Id
     *
     * @return
     */
    Integer getId();

    /**
     * 账号类型
     *
     * @return
     */
    AccountType getAccountType();

    List<String> getMenus();

}
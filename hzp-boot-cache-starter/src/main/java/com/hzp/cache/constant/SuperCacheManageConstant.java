package com.hzp.cache.constant;

import com.hzp.web.constant.AccountType;
import com.hzp.web.model.Account ;

import java.util.Arrays;
import java.util.List;

/**
 * @author wangchun
 * @date 2020/8/19
 */
public class SuperCacheManageConstant {
    public static final String SUPER_CACHE_MANAGE_PRIVILEGE = "super_cache_manage";
    public static final String SUPER_CACHE_NAMAGE_APP_ID = "superCacheManage";
    public static final String SUPER_CACHE_NAMAGE_AUTH_CODE = "hzp@@cache";
    public static final SupperCacheManageUserInfo supperCacheManageUserInfo = new SupperCacheManageUserInfo();

    private static class SupperCacheManageUserInfo implements Account {
        @Override
        public Integer getId() {
            return 1;
        }

        @Override
        public AccountType getAccountType() {
            return AccountType.User;
        }

        @Override
        public List<String> getMenus() {
            return Arrays.asList(SUPER_CACHE_MANAGE_PRIVILEGE);
        }
    }
}
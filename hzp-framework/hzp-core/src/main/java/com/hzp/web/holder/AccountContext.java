package com.hzp.web.holder;


import com.hzp.web.component.ThreadLocalHelper;
import com.hzp.web.model.Account;

/**
 * 用户上下文
 *
 * @author yxy
 * @date 2019/12/05 14:05
 **/
public class AccountContext {

    private static final String KEY = "USER_ACCOUNT";

    public static Account getAccount() {
        return ThreadLocalHelper.get(KEY);
    }

    public static void setAccount(Account account) {
        ThreadLocalHelper.put(KEY, account);
    }

    public static void remove() {
        ThreadLocalHelper.remove(KEY);
    }

}
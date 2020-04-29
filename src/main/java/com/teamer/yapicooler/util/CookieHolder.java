package com.teamer.yapicooler.util;

import org.apache.http.Header;

/**
 * 登录cookie保持器
 *
 * @author tanzj
 */
public class CookieHolder {

    private static volatile CookieHolder cookieHolder;

    public Header[] getCookies() {
        return cookies;
    }

    private Header[] cookies;

    private CookieHolder() {
    }

    public static CookieHolder getInstance() {
        if (cookieHolder == null) {
            synchronized (CookieHolder.class) {
                if (cookieHolder == null) {
                    System.out.println("初始化cookie!");
                    cookieHolder = new CookieHolder();
                }
            }
        }
        return cookieHolder;
    }

    public void setCookies(Header[] cookies) {
        this.cookies = cookies;
    }
}

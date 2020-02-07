package com.teamer.yapicooler.util;

import org.apache.http.Header;

/**
 * 登录cookie保持器
 * @author tanzj
 */
public class CookieHolder {

    private static CookieHolder cookieHolder;

    public Header[] getCookies() {
        return cookies;
    }

    private Header[] cookies;

    private CookieHolder() {
    }

    public static CookieHolder getInstance() {
        synchronized (CookieHolder.class) {
            if (cookieHolder == null) {
                return new CookieHolder();
            } else {
                return cookieHolder;
            }
        }
    }

    public void setCookies(Header[] cookies) {
        this.cookies = cookies;
    }
}

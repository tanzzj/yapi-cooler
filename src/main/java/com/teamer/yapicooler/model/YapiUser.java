package com.teamer.yapicooler.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * yapi用户实体类
 * @author tanzj
 */
@Data
@Accessors(chain = true)
public class YapiUser {

    /**
     * 用户名
     */
    private String email;
    /**
     * 用户密码
     */
    private String password;

}

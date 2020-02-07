package com.teamer.yapicooler.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * yapi组实体
 *
 * @author tanzj
 */
@Data
@Accessors(chain = true)
public class Group {

    /**
     * 组id
     */
    @JSONField(name = "_id")
    private String id;
    /**
     * 组名
     */
    @JSONField(name = "group_name")
    private String groupName;

}

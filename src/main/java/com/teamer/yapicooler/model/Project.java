package com.teamer.yapicooler.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * yapi项目实体
 *
 * @author tanzj
 */
@Data
@Accessors(chain = true)
public class Project {

    /**
     * projectId
     */
    @JSONField(name = "_id")
    private String id;
    /**
     * 项目名
     */
    private String name;

    /**
     * 项目所属组id
     */
    @JSONField(name = "group_id")
    private int groupId;

    @JSONField(name = "group_name")
    private String groupName;

}

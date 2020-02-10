package com.teamer.yapicooler.cooler;

import com.teamer.yapicooler.model.Group;
import com.teamer.yapicooler.model.Project;
import com.teamer.yapicooler.model.YapiUser;

import java.io.IOException;
import java.util.List;

/**
 * @author tanzj
 */
public interface YapiCooler {

    /**
     * yapi登录业务逻辑
     *
     * @param user (
     *             email-登录用户邮箱,
     *             password-用户密码
     *             )
     * @return String (success/fail)
     * @throws IOException IO异常
     */
    String login(YapiUser user) throws IOException;

    /**
     * 取出当前用户组列表
     *
     * @return List 组列表
     * @throws IOException IO异常
     */
    List<Group> getGroupList() throws IOException;

    /**
     * 根据组列表取出组项目列表并备份
     *
     * @param groupList 组列表
     * @return List yapi组项目列表
     * @throws IOException IO异常
     */
    List<Project> getGroupProject(List<Group> groupList) throws IOException;

    /**
     * 根据projectList导出并持久化项目接口文件
     *
     * @param nowDateTime 备份时间
     * @param project     (
     *                    id - 项目id
     *                    name - 项目名
     *                    groupId - 组id
     *                    groupName - 组名
     *                    )
     * @throws IOException IO异常
     */
    void backup(String nowDateTime, Project project) throws IOException;
}

package com.teamer.yapicooler.cooler.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.teamer.yapicooler.cooler.Test;
import com.teamer.yapicooler.cooler.YapiCooler;
import com.teamer.yapicooler.model.Group;
import com.teamer.yapicooler.model.Project;
import com.teamer.yapicooler.model.YapiUser;
import com.teamer.yapicooler.util.Constant;
import com.teamer.yapicooler.util.CookieHolder;
import com.teamer.yapicooler.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import static com.teamer.yapicooler.util.Constant.*;
import static org.apache.http.cookie.SM.COOKIE;
import static org.apache.http.cookie.SM.SET_COOKIE;

/**
 * @author tanzj
 */
@Slf4j
@Component
public class YapiCoolerImpl implements YapiCooler {


    @Autowired
    Test test;
    /**
     * cookie保持器
     */
    private CookieHolder cookieHolder = CookieHolder.getInstance();
    /**
     * http请求工具
     */
    final HttpUtil httpUtil;
    /**
     * 备份文件输出路径
     */
    @Value("${yapi.outputPath}")
    private String outputPath;


    @Autowired
    public YapiCoolerImpl(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    /**
     * yapi登录业务逻辑
     *
     * @param user (
     *             email-登录用户邮箱,
     *             password-用户密码
     *             )
     * @throws IOException IO异常
     */
    @Override
    public String login(YapiUser user) throws IOException {
        HttpResponse loginResponse = httpUtil.doPost("/api/user/login", user);
        String loginResponseString = EntityUtils.toString(loginResponse.getEntity());
        if (!YAPI_SUCCESS_CODE.equals(JSON.parseObject(loginResponseString).get(ERROR_MESSAGE))) {
            log.error(loginResponseString);
            return Constant.FAIL;
        }
        List<Header> headersList = new LinkedList<>();

        //将set-Cookie转为为cookie
        for (Header header : loginResponse.getAllHeaders()) {
            if (SET_COOKIE.equals(header.getName())) {
                headersList.add(new BasicHeader(COOKIE, header.getValue()));
            }
        }
        //拿出cookie作为全局变量
        cookieHolder.setCookies(headersList.toArray(new Header[0]));
        return Constant.SUCCESS;
    }

    /**
     * 取出当前用户组列表
     *
     * @return List 组列表
     * @throws IOException IO异常
     */
    @Override
    public List<Group> getGroupList() throws IOException {
        //拿到组列表
        HttpResponse getGroupResponse = httpUtil.doGet("/api/group/list", cookieHolder.getCookies());
        String groupResponseString = EntityUtils.toString(getGroupResponse.getEntity());
        JSONArray groupArrayList = JSON.parseObject(groupResponseString).getJSONArray(DATA_MESSAGE);

        //存放封装好的组，并抽取关键数据
        List<Group> groupLinkedList = new LinkedList<>();
        for (Object object : groupArrayList) {
            groupLinkedList.add(JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(object)), Group.class));
        }
        return groupLinkedList;
    }

    /**
     * 根据组列表取出组项目列表并备份
     *
     * @param groupList 组列表
     * @throws IOException IO异常
     */
    @Override
    public void backupGroupProject(List<Group> groupList) throws IOException {
        //循环进组
        for (Group group : groupList) {
            //组项目列表
            List<Project> projectList = new LinkedList<>();
            //请求组内项目列表
            HttpResponse getProjectResult = httpUtil.doGet("/api/project/list?group_id=" + group.getId() + "&page=1&limit=10", cookieHolder.getCookies());
            JSONArray projectJsonArray = JSON.parseObject(EntityUtils.toString(getProjectResult.getEntity())).getJSONObject(DATA_MESSAGE).getJSONArray("list");

            //构建组项目列表
            for (Object object : projectJsonArray) {
                Project project = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(object)), Project.class);
                projectList.add(project.setGroupName(group.getGroupName()));
            }
            //循环调用导出数据接口并持久化
            String nowDateTime = new SimpleDateFormat("yyyyMMddHHmm").format(System.currentTimeMillis());
            for (Project project : projectList) {
                test.backup(nowDateTime, project);
                System.out.println(project.getName() + "完成备份");
            }
        }
    }



}

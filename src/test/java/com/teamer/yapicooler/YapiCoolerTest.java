package com.teamer.yapicooler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.teamer.yapicooler.cooler.YapiCooler;
import com.teamer.yapicooler.model.Project;
import com.teamer.yapicooler.model.YapiUser;
import com.teamer.yapicooler.util.CookieHolder;
import com.teamer.yapicooler.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.teamer.yapicooler.util.Constant.DATA_MESSAGE;

@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class YapiCoolerTest {

    @Autowired
    YapiCooler YAPICooler;
    @Value("${yapi.adminUsername}")
    private String username;
    @Value("${yapi.adminPassword}")
    private String password;


    @Test
    public void doCooler() throws IOException {
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()) + "接口文档备份开始");

        YAPICooler.login(new YapiUser()
                .setEmail(username)
                .setPassword(password)
        );

        //取出组并备份组项目
        YAPICooler.backupGroupProject(YAPICooler.getGroupList());

        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()) + "接口文档备份完成");
    }


    @Autowired
    HttpUtil httpUtil;

    /**
     * 批量创建
     *
     * @throws IOException IO异常
     */
    @Test
    public void doCreate() throws IOException {

        YAPICooler.login(new YapiUser()
                .setEmail(username)
                .setPassword(password)
        );

        int i = 0;
        Map<String, String> createParams = new HashMap<>();
        //groupId自定义
        createParams.put("group_id", "49");
        createParams.put("icon", "code-o");
        createParams.put("color", "green");
        createParams.put("project_type", "private");
        while (i < 10) {
            createParams.put("name", UUID.randomUUID().toString().substring(10));
            httpUtil.doPost("/api/project/add", createParams, CookieHolder.getInstance().getCookies());
            i++;
        }
    }

    /**
     * 批量删除
     *
     * @throws IOException io异常
     */
    @Test
    public void doDelete() throws IOException {

        YAPICooler.login(new YapiUser()
                .setEmail(username)
                .setPassword(password)
        );

        //请求组内项目列表  groupId自定义
        HttpResponse getProjectResult = httpUtil.doGet("/api/project/list?group_id=" + 49 + "&page=1&limit=10", CookieHolder.getInstance().getCookies());
        JSONArray projectJsonArray = JSON.parseObject(EntityUtils.toString(getProjectResult.getEntity())).getJSONObject(DATA_MESSAGE).getJSONArray("list");
        //组项目列表
        List<Project> projectList = new LinkedList<>();
        //构建组项目列表
        for (Object object : projectJsonArray) {
            Project project = JSON.toJavaObject(JSON.parseObject(JSON.toJSONString(object)), Project.class);
            Map<String, String> deleteParams = new HashMap<>();
            deleteParams.put("id", project.getId());
            httpUtil.doPost("/api/project/del", deleteParams, CookieHolder.getInstance().getCookies());
        }
    }
}

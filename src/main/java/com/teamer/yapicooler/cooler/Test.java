package com.teamer.yapicooler.cooler;

import com.teamer.yapicooler.model.Project;
import com.teamer.yapicooler.util.CookieHolder;
import com.teamer.yapicooler.util.HttpUtil;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.io.File.separator;

/**
 * 备份方法
 *
 * @author tanzj
 */
@Component
public class Test {
    /**
     * cookie保持器
     */
    private CookieHolder cookieHolder = CookieHolder.getInstance();

    /**
     * http封装工具类
     */
    final HttpUtil httpUtil;
    /**
     * 备份文件输出路径
     */
    @Value("${yapi.outputPath}")
    private String outputPath;

    public Test(HttpUtil httpUtil) {
        this.httpUtil = httpUtil;
    }

    /**
     * 根据projectList导出并持久化项目接口文件
     *
     * @param nowDateTime 项目列表
     * @throws IOException IO异常
     */
    @Async
    public void backup(String nowDateTime, Project project) throws IOException {
        System.out.println(cookieHolder.hashCode());
        HttpResponse exportResult = httpUtil.doGet("/api/plugin/export?type=json&pid=" + project.getId() + "&status=all&isWiki=false", cookieHolder.getCookies());
        String jsonString = EntityUtils.toString(exportResult.getEntity(), StandardCharsets.UTF_8);
        //用户自定义路径+当前时间+组名+项目名
        File file = new File(outputPath + separator + nowDateTime + separator + project.getGroupName() + separator + project.getName() + ".json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        if (file.exists()) {
            Files.delete(Paths.get(file.getPath()));
        }
        file.createNewFile();
        try (Writer write = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8)) {
            write.write(jsonString);
            write.flush();
        }
    }
}

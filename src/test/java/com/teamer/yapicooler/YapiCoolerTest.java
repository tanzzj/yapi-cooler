package com.teamer.yapicooler;

import com.teamer.yapicooler.cooler.YapiCooler;
import com.teamer.yapicooler.model.YapiUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.SimpleDateFormat;

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

}

package com.teamer.yapicooler.schedule;

import com.teamer.yapicooler.cooler.YapiCooler;
import com.teamer.yapicooler.model.YapiUser;
import com.teamer.yapicooler.util.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 定时任务备份
 *
 * @author tanzj
 */
@Slf4j
@Component
public class ScheduleBackup {


    final YapiCooler yapiCooler;
    @Value("${yapi.adminUsername}")
    private String username;
    @Value("${yapi.adminPassword}")
    private String password;

    @Autowired
    public ScheduleBackup(YapiCooler yapiCooler) {
        this.yapiCooler = yapiCooler;
    }

    @Scheduled(cron = "${cooler.schedule}")
    public void yapiScheduleBackup() throws IOException {
        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()) + "接口文档备份开始");

        String loginResult = yapiCooler.login(new YapiUser()
                .setEmail(username)
                .setPassword(password));
        if (Constant.FAIL.equals(loginResult)){
            log.error("登录失败");
            return;
        }
        //取出组并备份组项目
        yapiCooler.backupGroupProject(yapiCooler.getGroupList());

        log.info(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()) + "接口文档备份完成");
    }


}

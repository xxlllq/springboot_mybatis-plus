package com.xxlllq.springboot_mybatisplus.config;

import com.xxlllq.springboot_mybatisplus.util.DateUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;

/**
 * @类名称： RegularTaskConfiguration
 * @类描述：系统中的相关定时任务（比如：数据库备份）
 * @创建人：xiangxl
 * @创建时间：2018/12/12 10:10
 * @version：
 */
@Configuration
@EnableScheduling
public class RegularTaskConfiguration {
    private Logger logger = Logger.getLogger(this.getClass());

    //数据库名
    @Value("${spring.datasource.name}")
    private String dbname;
    //数据库用户名
    @Value("${spring.datasource.username}")
    private String username;
    //数据库密码
    @Value("${spring.datasource.password}")
    private String password;
    //数据库密码
    @Value("${server.db-back-path}")
    private String dbbackpath;

    /**
     * 每天23:55执行一次数据库备份
     */
    @Scheduled(cron = "0 55 23 ? * *")
    public void dbBack() {
        logger.info("开始定时备份数据库：" + DateUtil.getTime());
        File uploadDir = new File(dbbackpath);
        if (!uploadDir.exists())
            uploadDir.mkdirs();

        String sql = "mysqldump -u" + username + "  -p" + password + " " + dbname + " >"
                + dbbackpath + dbname + DateUtil.dbTime() + ".sql";
        try {
            String[] command = {"cmd", "/c", sql};
            Runtime.getRuntime().exec(command);
            logger.info("备份数据库成功：" + DateUtil.getTime());
        } catch (Exception e) {
            logger.error("备份数据库失败!", e);
        }
    }

    //可多个，自己定义就行
}

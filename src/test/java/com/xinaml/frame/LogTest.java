package com.xinaml.frame;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = AppRoot.class)
@WebAppConfiguration
public class LogTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void log(){
        logger.info("写入info日志文件");
        logger.error("写入error日志文件");
        logger.warn("写入warn日志文件");
    }

}

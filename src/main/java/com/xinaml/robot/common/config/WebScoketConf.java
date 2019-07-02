package com.xinaml.robot.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @Author: [lgq]
 * @Date: [19-7-2 上午9:02]
 * @Description:
 * @Version: [1.0.0]
 * @Copy: [com.changbei]
 */
@Configuration
public class WebScoketConf {

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}

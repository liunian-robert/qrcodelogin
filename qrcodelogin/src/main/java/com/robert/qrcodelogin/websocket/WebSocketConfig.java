package com.robert.qrcodelogin.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/9/30 15:57
 * @描述: 如果是非springboot项目，此类可去除
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
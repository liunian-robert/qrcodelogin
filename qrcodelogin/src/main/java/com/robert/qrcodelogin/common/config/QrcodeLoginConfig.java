package com.robert.qrcodelogin.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/9/30 16:38
 * @描述:
 */
@Component
@ConfigurationProperties(prefix="qrcode")
@PropertySource("classpath:config.properties")
public class QrcodeLoginConfig {

    //二维码扫描地址
    private String login_scan_url;

    //第三方App android扫描对应跳转url(一般会配置为app在应用市场的下载地址)
    private String third_android_scan_Url;

    //第三方App android扫描对应跳转url(一般会配置为app在appstore的下载地址)
    private String third_iphone_scan_Url;

    //二维码失效时间(单位:分钟)
    private int expired_time;

    //项目访问地址
    private String server_url;


    public String getLogin_scan_url() {
        return login_scan_url;
    }

    public void setLogin_scan_url(String login_scan_url) {
        this.login_scan_url = login_scan_url;
    }

    public String getThird_android_scan_Url() {
        return third_android_scan_Url;
    }

    public void setThird_android_scan_Url(String third_android_scan_Url) {
        this.third_android_scan_Url = third_android_scan_Url;
    }

    public String getThird_iphone_scan_Url() {
        return third_iphone_scan_Url;
    }

    public void setThird_iphone_scan_Url(String third_iphone_scan_Url) {
        this.third_iphone_scan_Url = third_iphone_scan_Url;
    }

    public int getExpired_time() {
        return expired_time;
    }

    public void setExpired_time(int expired_time) {
        this.expired_time = expired_time;
    }

    public String getServer_url() {
        return server_url;
    }

    public void setServer_url(String server_url) {
        this.server_url = server_url;
    }
}

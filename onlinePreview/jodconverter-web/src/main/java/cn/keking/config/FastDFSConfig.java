package cn.keking.config;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/9/28 09:58
 * @描述:
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="fastdfs")
@PropertySource("classpath:config.properties")
public class FastDFSConfig {

    private int connect_timeout_in_seconds;
    private int network_timeout_in_seconds;
    private String charset;
    private Boolean http_anti_steal_token;
    private String http_secret_key;
    private int http_tracker_http_port;
    private String tracker_servers;
    private String file_prefix;

    public int getConnect_timeout_in_seconds() {
        return connect_timeout_in_seconds;
    }

    public void setConnect_timeout_in_seconds(int connect_timeout_in_seconds) {
        this.connect_timeout_in_seconds = connect_timeout_in_seconds;
    }

    public int getNetwork_timeout_in_seconds() {
        return network_timeout_in_seconds;
    }

    public void setNetwork_timeout_in_seconds(int network_timeout_in_seconds) {
        this.network_timeout_in_seconds = network_timeout_in_seconds;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Boolean getHttp_anti_steal_token() {
        return http_anti_steal_token;
    }

    public void setHttp_anti_steal_token(Boolean http_anti_steal_token) {
        this.http_anti_steal_token = http_anti_steal_token;
    }

    public String getHttp_secret_key() {
        return http_secret_key;
    }

    public void setHttp_secret_key(String http_secret_key) {
        this.http_secret_key = http_secret_key;
    }

    public int getHttp_tracker_http_port() {
        return http_tracker_http_port;
    }

    public void setHttp_tracker_http_port(int http_tracker_http_port) {
        this.http_tracker_http_port = http_tracker_http_port;
    }

    public String getTracker_servers() {
        return tracker_servers;
    }

    public void setTracker_servers(String tracker_servers) {
        this.tracker_servers = tracker_servers;
    }

    public String getFile_prefix() {
        return file_prefix;
    }

    public void setFile_prefix(String file_prefix) {
        this.file_prefix = file_prefix;
    }
}

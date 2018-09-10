package com.robert.qrcodelogin.bean;

import io.swagger.annotations.ApiModelProperty;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/6/27 17:51
 * @描述:
 */
public class QRCodeToken {
    @ApiModelProperty(notes = "二维码登录时的唯一标识")
    private String token;
    @ApiModelProperty(notes = "二维码失效时间")
    private long expireTime;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QRCodeToken{");
        sb.append("token='").append(token).append('\'');
        sb.append(", expireTime=").append(expireTime);
        sb.append('}');
        return sb.toString();
    }
}

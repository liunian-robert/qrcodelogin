package com.robert.qrcodelogin.bean;

import io.swagger.annotations.ApiModelProperty;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/6/27 13:46
 * @描述:
 */
public class QRCode {
    @ApiModelProperty(notes = "二维码登录时的唯一标识")
    private String token;
    @ApiModelProperty(notes = "二维码base64编码")
    private String base64Qrcode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getBase64Qrcode() {
        return base64Qrcode;
    }

    public void setBase64Qrcode(String base64Qrcode) {
        this.base64Qrcode = base64Qrcode;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QRCode{");
        sb.append("token='").append(token).append('\'');
        sb.append(", base64Qrcode='").append(base64Qrcode).append('\'');
        sb.append('}');
        return sb.toString();
    }
}



package com.robert.qrcodelogin.bean;

/**
 * @创建人: zhangyapo
 * @创建时间: 2018/6/28 14:40
 * @描述:
 */
public class QRCodeUser {
    private String username;
    private Boolean isAuthorize = Boolean.FALSE;//默认未授权
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public Boolean getAuthorize() {
        return isAuthorize;
    }

    public void setAuthorize(Boolean authorize) {
        isAuthorize = authorize;
    }


    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("QRCodeUser{");
        sb.append("username='").append(username).append('\'');
        sb.append(", isAuthorize=").append(isAuthorize);
        sb.append('}');
        return sb.toString();
    }
}

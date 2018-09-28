package com.robert.qrcodelogin.bean;


import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: qijun
 * Email: 18353367683@163.com
 * Date: 2017-07-10 13:07
 * Version: 1.0.0
 * Description:
 */
public class User implements Serializable {

	public static final int CAS=1;
	
	public static final int WEB=2;

	public static final int QRCODE=3;
	
	
    private static final long serialVersionUID = -7026760576873457093L;
    

    private String username;//用户名

    private String password;//密码

    private String mobile;//手机号

    private int loginType;//登录类型 1 表示通行证 2表示手机号

    private Date firstLoginTime;//开始登录时间

    private Date lastLogoutTime;//最后登出时间

    private long ip;//登录ip

    private String agent;//浏览器标识
    
    /**
     * 登陆的方式，目前有CAS和WEB两种
     * myluban1.3添加扫码登录
     */
    private Integer type=2;


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFirstLoginTime() {
        return firstLoginTime;
    }

    public void setFirstLoginTime(Date firstLoginTime) {
        this.firstLoginTime = firstLoginTime;
    }

    public Date getLastLogoutTime() {
        return lastLogoutTime;
    }

    public void setLastLogoutTime(Date lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
    }

    public long getIp() {
        return ip;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
    
    
}
